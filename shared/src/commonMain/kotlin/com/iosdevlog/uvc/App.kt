package com.iosdevlog.uvc

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.iosdevlog.uvc.domain.repository.createUVCRepository
import com.iosdevlog.uvc.platform.VideoRecorder
import com.iosdevlog.uvc.presentation.screens.CameraListScreen
import com.iosdevlog.uvc.presentation.screens.CameraSettings
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
        val recorder = remember { VideoRecorder() }

        var selectedDeviceId by remember { mutableStateOf<String?>(null) }
        var currentFrame by remember { mutableStateOf(null as com.iosdevlog.uvc.domain.model.VideoFrame?) }
        var isRecording by remember { mutableStateOf(false) }
        var cameraSettings by remember { mutableStateOf(CameraSettings()) }

        LaunchedEffect(selectedDeviceId) {
            selectedDeviceId?.let { deviceId ->
                repository.getVideoStream(deviceId).collectLatest { frame ->
                    currentFrame = frame
                    // Write frame to recorder if recording
                    if (isRecording) {
                        recorder.writeFrame(frame.data)
                    }
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
                isRecording = isRecording,
                cameraSettings = cameraSettings,
                onCameraSettingsChange = { newSettings ->
                    cameraSettings = newSettings
                    println("Camera settings updated: $newSettings")
                    // TODO: Apply settings to camera via CameraController
                },
                onDisconnect = {
                    scope.launch {
                        if (isRecording) {
                            recorder.stopRecording()
                            isRecording = false
                        }
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
                },
                onToggleRecording = {
                    scope.launch {
                        if (isRecording) {
                            recorder.stopRecording()
                            isRecording = false
                        } else {
                            recorder.startRecording()
                            isRecording = true
                        }
                    }
                }
            )
        }
    }
}