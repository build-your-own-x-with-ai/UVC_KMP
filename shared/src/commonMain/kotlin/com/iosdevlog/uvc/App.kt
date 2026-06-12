package com.iosdevlog.uvc

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.iosdevlog.uvc.domain.repository.createUVCRepository
import com.iosdevlog.uvc.presentation.screens.CameraListScreen
import com.iosdevlog.uvc.presentation.screens.PreviewScreen
import com.iosdevlog.uvc.presentation.viewmodel.CameraListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun App() {
    MaterialTheme {
        val repository = remember { createUVCRepository() }
        val viewModel = remember { CameraListViewModel(repository) }
        val devices by viewModel.devices.collectAsState()
        val scope = rememberCoroutineScope()

        var selectedDeviceId by remember { mutableStateOf<String?>(null) }
        var currentFrame by remember { mutableStateOf(null as com.iosdevlog.uvc.domain.model.VideoFrame?) }

        LaunchedEffect(selectedDeviceId) {
            selectedDeviceId?.let { deviceId ->
                repository.getVideoStream(deviceId).collectLatest { frame ->
                    currentFrame = frame
                }
            }
        }

        if (selectedDeviceId == null) {
            CameraListScreen(
                devices = devices,
                onDeviceClick = { device ->
                    scope.launch {
                        repository.connect(device.id)
                        selectedDeviceId = device.id
                    }
                },
                onRefresh = { viewModel.refresh() }
            )
        } else {
            val device = devices.find { it.id == selectedDeviceId }
            PreviewScreen(
                deviceName = device?.name ?: "Unknown",
                currentFrame = currentFrame,
                onDisconnect = {
                    scope.launch {
                        selectedDeviceId?.let { repository.disconnect(it) }
                        selectedDeviceId = null
                        currentFrame = null
                    }
                },
                onCaptureScreenshot = {
                    currentFrame?.let { frame ->
                        scope.launch {
                            captureScreenshot(frame)
                        }
                    }
                }
            )
        }
    }
}