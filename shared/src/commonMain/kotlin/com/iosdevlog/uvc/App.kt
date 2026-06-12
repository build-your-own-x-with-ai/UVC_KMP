package com.iosdevlog.uvc

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.iosdevlog.uvc.domain.repository.createUVCRepository
import com.iosdevlog.uvc.presentation.screens.CameraListScreen
import com.iosdevlog.uvc.presentation.screens.PreviewScreen
import com.iosdevlog.uvc.presentation.viewmodel.CameraListViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun App() {
    MaterialTheme {
        val repository = remember { createUVCRepository() }
        val viewModel = remember { CameraListViewModel(repository) }
        val devices by viewModel.devices.collectAsState()

        var selectedDeviceId by remember { mutableStateOf<String?>(null) }

        if (selectedDeviceId == null) {
            CameraListScreen(
                devices = devices,
                onDeviceClick = { device ->
                    selectedDeviceId = device.id
                },
                onRefresh = { viewModel.refresh() }
            )
        } else {
            val device = devices.find { it.id == selectedDeviceId }
            PreviewScreen(
                deviceName = device?.name ?: "Unknown",
                currentFrame = null,
                onDisconnect = { selectedDeviceId = null }
            )
        }
    }
}