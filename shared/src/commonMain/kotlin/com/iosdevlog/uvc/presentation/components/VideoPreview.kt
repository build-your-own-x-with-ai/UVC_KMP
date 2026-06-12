package com.iosdevlog.uvc.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iosdevlog.uvc.domain.model.VideoFrame

@Composable
expect fun VideoPreview(
    frame: VideoFrame?,
    modifier: Modifier = Modifier
)
