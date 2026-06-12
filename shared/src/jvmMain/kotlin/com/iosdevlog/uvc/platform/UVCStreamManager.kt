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
        println("uvc_find_device result: $result")
        if (result != 0) throw Exception("uvc_find_device failed: $result")

        val device = devRef.value
        if (device == null) throw Exception("Device pointer is null")

        val devhRef = PointerByReference()
        val openResult = libuvc.uvc_open(device, devhRef)
        println("uvc_open result: $openResult")
        if (openResult != 0) {
            throw Exception("uvc_open failed: $openResult (error -3 = permission denied, check System Settings > Privacy > Camera)")
        }
        devHandle = devhRef.value
    }

    fun startStreaming(width: Int = 640, height: Int = 480, fps: Int = 30): Result<Unit> = runCatching {
        val devh = devHandle ?: throw Exception("Camera not opened")
        val ctrl = Memory(256)

        // Try different resolutions for H.264
        val configs = listOf(
            Triple(1920, 1080, 30),
            Triple(1280, 720, 30),
            Triple(640, 480, 30),
            Triple(640, 480, 15)
        )

        for ((w, h, f) in configs) {
            val result = libuvc.uvc_get_stream_ctrl_format_size(
                devh, ctrl, LibUVC.UVC_FRAME_FORMAT_H264, w, h, f
            )
            println("H264 ${w}x${h}@${f}fps result: $result")
            if (result == 0) {
                println("Using H264: ${w}x${h}@${f}fps")
                return startStreamingWithFormat(devh, ctrl, VideoFormat.H264)
            }
        }

        throw Exception("H264 not supported with any tested resolution")
    }

    private fun startStreamingWithFormat(devh: Pointer, ctrl: Pointer, format: VideoFormat): Result<Unit> = runCatching {

        val callback = object : FrameCallback {
            override fun invoke(frame: UVCFrame?, user_ptr: Pointer?) {
                frame?.let {
                    println("Frame received: ${it.width}x${it.height}, ${it.data_bytes} bytes, format=$format")
                    val data = it.data?.getByteArray(0, it.data_bytes) ?: return
                    frameChannel.trySend(
                        VideoFrame(data, it.width, it.height, format, System.currentTimeMillis())
                    )
                }
            }
        }

        val callbackPtr = com.sun.jna.CallbackReference.getFunctionPointer(callback)
        val streamResult = libuvc.uvc_start_streaming(devh, ctrl, callbackPtr, null, 0)
        if (streamResult != 0) throw Exception("uvc_start_streaming failed: $streamResult")
        println("Streaming started successfully with format: $format")
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
