package com.iosdevlog.uvc.platform

import com.iosdevlog.uvc.domain.model.VideoFormat
import com.iosdevlog.uvc.domain.model.VideoFrame
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.imageio.ImageIO

object ScreenshotSaver {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")

    fun saveScreenshot(frame: VideoFrame): Result<String> = runCatching {
        val image = when (frame.format) {
            VideoFormat.MJPEG -> MJPEGDecoder.decode(frame.data, frame.width, frame.height)
            VideoFormat.H264 -> H264Decoder.decode(frame.data, frame.width, frame.height)
            else -> null
        } ?: throw Exception("Failed to decode frame")

        val timestamp = dateFormat.format(Date())
        val filename = "UVC_Screenshot_$timestamp.png"
        val desktopPath = System.getProperty("user.home") + "/Desktop"
        val outputFile = File(desktopPath, filename)

        ImageIO.write(image, "PNG", outputFile)

        println("Screenshot saved: ${outputFile.absolutePath}")
        outputFile.absolutePath
    }
}
