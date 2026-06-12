package com.iosdevlog.uvc.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import com.iosdevlog.uvc.domain.model.VideoFormat
import com.iosdevlog.uvc.domain.model.VideoFrame
import com.iosdevlog.uvc.platform.H264Decoder
import com.iosdevlog.uvc.platform.MJPEGDecoder
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JPanel

@Composable
actual fun VideoPreview(
    frame: VideoFrame?,
    modifier: Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (frame == null) {
            Text("No video frame")
        } else {
            val image = remember(frame) {
                when (frame.format) {
                    VideoFormat.MJPEG -> MJPEGDecoder.decode(frame.data, frame.width, frame.height)
                    VideoFormat.H264 -> H264Decoder.decode(frame.data, frame.width, frame.height)
                    else -> null
                }
            }

            if (image != null) {
                // Calculate aspect ratio to preserve original proportions
                val aspectRatio = image.width.toFloat() / image.height.toFloat()

                SwingPanel(
                    modifier = Modifier.aspectRatio(aspectRatio).fillMaxWidth(),
                    factory = { VideoPanel(image) },
                    update = { it.updateImage(image) }
                )
            } else {
                Text("Failed to decode frame")
            }
        }
    }
}

private class VideoPanel(private var image: BufferedImage) : JPanel() {
    fun updateImage(newImage: BufferedImage) {
        image = newImage
        repaint()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.drawImage(image, 0, 0, width, height, null)
    }
}
