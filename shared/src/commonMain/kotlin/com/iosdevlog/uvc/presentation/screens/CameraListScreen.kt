package com.iosdevlog.uvc.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iosdevlog.uvc.domain.model.UVCDevice

@Composable
fun CameraListScreen(
    devices: List<UVCDevice>,
    onDeviceClick: (UVCDevice) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = onRefresh) {
                Text("↻")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text(
                text = "UVC Cameras",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            if (devices.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No UVC cameras detected")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(devices) { device ->
                        DeviceCard(device = device, onClick = { onDeviceClick(device) })
                    }
                }
            }
        }
    }
}

@Composable
private fun DeviceCard(device: UVCDevice, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = device.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "VID: ${device.vendorId.toString(16)} / PID: ${device.productId.toString(16)}",
                style = MaterialTheme.typography.bodySmall
            )
            device.serialNumber?.let {
                Text(text = "Serial: $it", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "Formats: ${device.supportedFormats.joinToString()}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
