package com.iosdevlog.uvc.platform

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

object H264Decoder {
    // H.264 需要外部解码器，Java 原生不支持
    // 简单方案：使用 JavaCV (FFmpeg wrapper)
    fun decode(data: ByteArray, width: Int, height: Int): BufferedImage? {
        // TODO: 集成 JavaCV 或使用系统 ffmpeg
        // 临时：尝试作为 JPEG 解码（某些相机会在 H.264 标记下发送 MJPEG）
        return try {
            ImageIO.read(ByteArrayInputStream(data))
        } catch (e: Exception) {
            null
        }
    }
}
