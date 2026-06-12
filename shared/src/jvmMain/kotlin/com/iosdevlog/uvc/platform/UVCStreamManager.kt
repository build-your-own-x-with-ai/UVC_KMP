package com.iosdevlog.uvc.platform

import com.iosdevlog.uvc.domain.model.VideoFormat
import com.iosdevlog.uvc.domain.model.VideoFrame
import com.iosdevlog.uvc.platform.jna.*
import com.sun.jna.Memory
import com.sun.jna.Pointer
import com.sun.jna.ptr.PointerByReference
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class UVCStreamManager {
    private val libuvc = LibUVC.INSTANCE
    private var context: Pointer? = null
    private var devHandle: Pointer? = null
    private val frameChannel = Channel<VideoFrame>(Channel.BUFFERED)

    fun init(): Result<Unit> = runCatching {
        val ctx = PointerByReference()
        val result = libuvc.uvc_init(ctx, null)
        if (result != 0) throw Exception("uvc_init failed: $result")
        context = ctx.value
    }

    fun openCamera(vid: Int, pid: Int): Result<Unit> = runCatching {
        val ctx = context ?: throw Exception("UVC not initialized")
        val devRef = PointerByReference()
        val result = libuvc.uvc_find_device(ctx, devRef, vid, pid, null)
        if (result != 0) throw Exception("uvc_find_device failed: $result")

        val devhRef = PointerByReference()
        val openResult = libuvc.uvc_open(devRef.value, devhRef)
        if (openResult != 0) throw Exception("uvc_open failed: $openResult")
        devHandle = devhRef.value
    }

    fun startStreaming(width: Int = 640, height: Int = 480, fps: Int = 30): Result<Unit> = runCatching {
        val devh = devHandle ?: throw Exception("Camera not opened")
        val ctrl = Memory(256)
        val result = libuvc.uvc_get_stream_ctrl_format_size(
            devh, ctrl, LibUVC.UVC_FRAME_FORMAT_MJPEG, width, height, fps
        )
        if (result != 0) throw Exception("uvc_get_stream_ctrl_format_size failed: $result")

        val callback = object : FrameCallback {
            override fun invoke(frame: UVCFrame?, user_ptr: Pointer?) {
                frame?.let {
                    val data = it.data?.getByteArray(0, it.data_bytes) ?: return
                    frameChannel.trySend(
                        VideoFrame(data, it.width, it.height, VideoFormat.MJPEG, System.currentTimeMillis())
                    )
                }
            }
        }

        val callbackPtr = com.sun.jna.CallbackReference.getFunctionPointer(callback)
        val streamResult = libuvc.uvc_start_streaming(devh, ctrl, callbackPtr, null, 0)
        if (streamResult != 0) throw Exception("uvc_start_streaming failed: $streamResult")
    }

    fun getFrames(): Flow<VideoFrame> = frameChannel.receiveAsFlow()

    fun stopStreaming() {
        devHandle?.let { libuvc.uvc_stop_streaming(it) }
    }

    fun close() {
        devHandle?.let { libuvc.uvc_close(it) }
        devHandle = null
        context?.let { libuvc.uvc_exit(it) }
        context = null
    }
}
