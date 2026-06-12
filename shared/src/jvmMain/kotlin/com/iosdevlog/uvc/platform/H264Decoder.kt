package com.iosdevlog.uvc.platform

import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import java.awt.image.BufferedImage
import java.io.*
import java.util.concurrent.ConcurrentLinkedQueue

object H264Decoder {
    private val converter = Java2DFrameConverter()
    private var grabber: FFmpegFrameGrabber? = null
    private var decoderThread: Thread? = null
    @Volatile private var latestFrame: BufferedImage? = null
    private val frameQueue = ConcurrentLinkedQueue<ByteArray>()
    private var frameCount = 0

    // Temp file for cumulative H.264 stream
    private val tempFile = File.createTempFile("uvc_stream_", ".h264")
    private var fileStream: FileOutputStream? = null

    init {
        try {
            fileStream = FileOutputStream(tempFile, false)
            println("H264Decoder: Temp stream file: ${tempFile.absolutePath}")
            println("H264Decoder: Real resolution: 1280x480 (from SPS)")

            // Start decoder thread
            decoderThread = Thread({
                try {
                    // Wait for first frame (SPS)
                    Thread.sleep(500)

                    grabber = FFmpegFrameGrabber(tempFile.absolutePath).apply {
                        format = "h264"
                        start()
                    }
                    println("H264Decoder: FFmpeg grabber started")

                    var lastPosition = 0L
                    while (!Thread.interrupted()) {
                        // Check if file has new data
                        val currentSize = tempFile.length()
                        if (currentSize > lastPosition) {
                            grabber?.grabImage()?.let { frame ->
                                latestFrame = converter.convert(frame)
                                if (frameCount % 30 == 0) {
                                    println("H264Decoder: Decoded frame ${frameCount/30}s")
                                }
                            }
                            lastPosition = currentSize
                        } else {
                            Thread.sleep(10)
                        }
                    }
                } catch (e: Exception) {
                    println("H264Decoder thread error: ${e.message}")
                    e.printStackTrace()
                }
            }, "H264-Decoder").apply {
                isDaemon = true
                start()
            }
        } catch (e: Exception) {
            println("H264Decoder init error: ${e.message}")
            e.printStackTrace()
        }
    }

    fun decode(data: ByteArray, width: Int, height: Int): BufferedImage? {
        return try {
            // Write to cumulative file
            fileStream?.write(data)
            fileStream?.flush()
            frameCount++

            if (frameCount % 30 == 0) {
                println("H264Decoder: Received ${frameCount} frames (${frameCount/30}s)")
            }

            // Return latest decoded frame
            latestFrame
        } catch (e: Exception) {
            println("H264Decoder error: ${e.message}")
            null
        }
    }

    fun close() {
        decoderThread?.interrupt()
        grabber?.stop()
        grabber?.release()
        fileStream?.close()
        tempFile.delete()
        println("H264Decoder: Closed")
    }
}
