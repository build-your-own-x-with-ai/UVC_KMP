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
    private var activeCallback: FrameCallback? = null  // Keep reference to prevent GC

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

        // Print supported formats
        printSupportedFormats(devhRef.value)
    }

    private fun printSupportedFormats(devh: Pointer) {
        println("\n=== Enumerating supported formats ===")

        // Try to get format descriptors
        val formatDescs = libuvc.uvc_get_format_descs(devh)
        if (formatDescs != null) {
            println("Format descriptors found at: $formatDescs")
        } else {
            println("No format descriptors available (uvc_get_format_descs returned null)")
        }

        val ctrl = Memory(256)
        val formats = listOf(
            LibUVC.UVC_FRAME_FORMAT_YUYV to "YUYV",
            LibUVC.UVC_FRAME_FORMAT_MJPEG to "MJPEG",
            LibUVC.UVC_FRAME_FORMAT_H264 to "H264",
            0 to "ANY(0)"
        )
        val resolutions = listOf(
            Triple(1920, 1080, 30), Triple(1280, 720, 30),
            Triple(640, 480, 30), Triple(640, 480, 15),
            Triple(320, 240, 30)
        )

        for ((formatId, formatName) in formats) {
            for ((w, h, f) in resolutions) {
                val result = libuvc.uvc_get_stream_ctrl_format_size(devh, ctrl, formatId, w, h, f)
                if (result == 0) {
                    println("✓ $formatName ${w}x${h}@${f}fps")
                } else if (formatId == 0 && result != -51) {
                    println("  ANY(0) ${w}x${h}@${f}fps -> error: $result")
                }
            }
        }
        println("=== End of format enumeration ===\n")
    }

    fun startStreaming(width: Int = 5184, height: Int = 1944, fps: Int = 30): Result<Unit> = runCatching {
        val devh = devHandle ?: throw Exception("Camera not opened")
        val ctrl = Memory(256)

        // Camera supports H.264 5184x1944@30fps
        val result = libuvc.uvc_get_stream_ctrl_format_size(
            devh, ctrl, LibUVC.UVC_FRAME_FORMAT_H264, width, height, fps
        )
        println("H264 ${width}x${height}@${fps}fps result: $result")
        if (result != 0) throw Exception("H264 format setup failed: $result")

        println("Using H264: ${width}x${height}@${fps}fps")
        return startStreamingWithFormat(devh, ctrl, VideoFormat.H264)
    }

    private fun startStreamingWithFormat(devh: Pointer, ctrl: Pointer, format: VideoFormat): Result<Unit> = runCatching {
        println("Starting stream with format: $format")

        activeCallback = object : FrameCallback {
            override fun invoke(framePtr: Pointer?, user_ptr: Pointer?) {
                println("Callback invoked! framePtr=$framePtr")
                if (framePtr == null) {
                    println("Frame pointer is null")
                    return
                }
                try {
                    val frame = UVCFrame(framePtr)
                    frame.read()

                    println("Frame: ${frame.width}x${frame.height}, ${frame.data_bytes.toLong()} bytes, seq=${frame.sequence}")
                    val data = frame.data?.getByteArray(0, frame.data_bytes.toInt()) ?: run {
                        println("Failed to read frame data")
                        return
                    }
                    val sent = frameChannel.trySend(
                        VideoFrame(data, frame.width, frame.height, format, System.currentTimeMillis())
                    )
                    println("Frame sent to channel: ${sent.isSuccess}")
                } catch (e: Exception) {
                    println("Callback error: ${e.message}")
                    e.printStackTrace()
                }
            }
        }

        val callbackPtr = com.sun.jna.CallbackReference.getFunctionPointer(activeCallback)
        println("Callback pointer: $callbackPtr")
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
