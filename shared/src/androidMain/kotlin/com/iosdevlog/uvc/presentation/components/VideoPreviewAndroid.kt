package com.iosdevlog.uvc.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.iosdevlog.uvc.domain.model.VideoFrame

@Composable
actual fun VideoPreview(
    frame: VideoFrame?,
    modifier: Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (frame == null) {
            Text("No video frame")
        } else {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(Color.Gray)
            }
        }
    }
}
