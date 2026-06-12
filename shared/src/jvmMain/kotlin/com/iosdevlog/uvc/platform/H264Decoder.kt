package com.iosdevlog.uvc.platform

import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.Java2DFrameConverter
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream

object H264Decoder {
    private val converter = Java2DFrameConverter()

    fun decode(data: ByteArray, width: Int, height: Int): BufferedImage? {
        return try {
            val input = ByteArrayInputStream(data)
            val grabber = FFmpegFrameGrabber(input)
            grabber.videoCodec = org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264
            grabber.imageWidth = width
            grabber.imageHeight = height
            grabber.format = "h264"
            grabber.start()

            val frame: Frame? = grabber.grabImage()
            grabber.stop()
            grabber.release()

            frame?.let { converter.convert(it) }
        } catch (e: Exception) {
            println("H264 decode error: ${e.message}")
            null
        }
    }
}
