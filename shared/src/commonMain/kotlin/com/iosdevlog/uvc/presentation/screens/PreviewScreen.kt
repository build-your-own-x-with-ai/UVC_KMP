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
    onToggleRecording: () -> Unit = {},
    isRecording: Boolean = false,
    cameraSettings: CameraSettings = CameraSettings(),
    onCameraSettingsChange: (CameraSettings) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showControls by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(deviceName) },
                actions = {
                    IconButton(onClick = onCaptureScreenshot) {
                        Text("📷")
                    }
                    IconButton(onClick = onToggleRecording) {
                        Text(if (isRecording) "⏹️" else "⏺️")
                    }
                    IconButton(onClick = { showControls = !showControls }) {
                        Text("⚙️")
                    }
                    TextButton(onClick = onDisconnect) {
                        Text("Disconnect")
                    }
                }
            )
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Video preview
            Box(
                modifier = Modifier
                    .weight(if (showControls) 0.7f else 1f)
                    .fillMaxHeight()
            ) {
                VideoPreview(
                    frame = currentFrame,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Controls panel
            if (showControls) {
                CameraControlsPanel(
                    settings = cameraSettings,
                    onSettingsChange = onCameraSettingsChange,
                    onReset = { onCameraSettingsChange(CameraSettings()) },
                    modifier = Modifier
                        .weight(0.3f)
                        .fillMaxHeight()
                )
            }
        }
    }
}
