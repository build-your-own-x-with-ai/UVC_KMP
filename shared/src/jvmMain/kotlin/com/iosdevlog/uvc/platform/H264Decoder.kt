package com.iosdevlog.uvc.platform

import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream

object H264Decoder {
    private val converter = Java2DFrameConverter()
    private var pipedOutput: PipedOutputStream? = null
    private var pipedInput: PipedInputStream? = null
    private var grabber: FFmpegFrameGrabber? = null
    private var decoderThread: Thread? = null
    private var latestFrame: BufferedImage? = null

    init {
        try {
            // Create pipe for H.264 stream
            pipedInput = PipedInputStream(1024 * 1024) // 1MB buffer
            pipedOutput = PipedOutputStream(pipedInput)

            grabber = FFmpegFrameGrabber(pipedInput).apply {
                videoCodec = org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264
                format = "h264"
                start()
            }

            // Decoder thread
            decoderThread = Thread {
                try {
                    while (!Thread.interrupted()) {
                        grabber?.grabImage()?.let { frame ->
                            latestFrame = converter.convert(frame)
                        }
                    }
                } catch (e: Exception) {
                    println("Decoder thread error: ${e.message}")
                }
            }.apply {
                isDaemon = true
                start()
            }

            println("H.264 decoder initialized with persistent state")
        } catch (e: Exception) {
            println("H.264 decoder init error: ${e.message}")
            e.printStackTrace()
        }
    }

    fun decode(data: ByteArray, width: Int, height: Int): BufferedImage? {
        return try {
            pipedOutput?.write(data)
            pipedOutput?.flush()
            latestFrame
        } catch (e: Exception) {
            println("H264 decode error: ${e.message}")
            null
        }
    }
}
