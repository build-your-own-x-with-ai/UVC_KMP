package com.iosdevlog.uvc.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iosdevlog.uvc.domain.model.VideoFrame
import com.iosdevlog.uvc.presentation.components.VideoPreview

@Composable
fun PreviewScreen(
    deviceName: String,
    currentFrame: VideoFrame?,
    onDisconnect: () -> Unit,
    onCaptureScreenshot: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(deviceName) },
                actions = {
                    IconButton(onClick = onCaptureScreenshot) {
                        Text("📷")
                    }
                    TextButton(onClick = onDisconnect) {
                        Text("Disconnect")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            VideoPreview(
                frame = currentFrame,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
