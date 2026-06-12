package com.iosdevlog.uvc.platform

import com.iosdevlog.uvc.domain.model.VideoFrame
import com.iosdevlog.uvc.domain.model.VideoFormat
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

object MJPEGDecoder {
    fun decode(data: ByteArray, width: Int, height: Int): BufferedImage? {
        return try {
            ImageIO.read(ByteArrayInputStream(data))
        } catch (e: Exception) {
            null
        }
    }

    fun toVideoFrame(data: ByteArray, width: Int, height: Int): VideoFrame? {
        return VideoFrame(
            data = data,
            width = width,
            height = height,
            format = VideoFormat.MJPEG,
            timestamp = System.currentTimeMillis()
        )
    }
}
