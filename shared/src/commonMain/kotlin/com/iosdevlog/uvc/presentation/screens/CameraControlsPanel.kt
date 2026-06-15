package com.iosdevlog.uvc.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class CameraSettings(
    val brightness: Float = 128f,
    val contrast: Float = 128f,
    val saturation: Float = 128f,
    val whiteBalance: Float = 4600f,
    val exposure: Float = 100f,
    val gain: Float = 50f,
    val sharpness: Float = 128f,
    val gamma: Float = 100f
)

@Composable
fun CameraControlsPanel(
    settings: CameraSettings,
    onSettingsChange: (CameraSettings) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Camera Controls",
                style = MaterialTheme.typography.titleMedium
            )

            // Brightness
            ControlSlider(
                label = "Brightness",
                value = settings.brightness,
                range = 0f..255f,
                onValueChange = { onSettingsChange(settings.copy(brightness = it)) }
            )

            // Contrast
            ControlSlider(
                label = "Contrast",
                value = settings.contrast,
                range = 0f..255f,
                onValueChange = { onSettingsChange(settings.copy(contrast = it)) }
            )

            // Saturation
            ControlSlider(
                label = "Saturation",
                value = settings.saturation,
                range = 0f..255f,
                onValueChange = { onSettingsChange(settings.copy(saturation = it)) }
            )

            // White Balance
            ControlSlider(
                label = "White Balance",
                value = settings.whiteBalance,
                range = 2800f..6500f,
                unit = "K",
                onValueChange = { onSettingsChange(settings.copy(whiteBalance = it)) }
            )

            // Exposure
            ControlSlider(
                label = "Exposure",
                value = settings.exposure,
                range = 1f..500f,
                onValueChange = { onSettingsChange(settings.copy(exposure = it)) }
            )

            // Gain
            ControlSlider(
                label = "Gain",
                value = settings.gain,
                range = 0f..100f,
                onValueChange = { onSettingsChange(settings.copy(gain = it)) }
            )

            // Sharpness
            ControlSlider(
                label = "Sharpness",
                value = settings.sharpness,
                range = 0f..255f,
                onValueChange = { onSettingsChange(settings.copy(sharpness = it)) }
            )

            // Gamma
            ControlSlider(
                label = "Gamma",
                value = settings.gamma,
                range = 50f..200f,
                onValueChange = { onSettingsChange(settings.copy(gamma = it)) }
            )

            // Reset button
            Button(
                onClick = onReset,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset to Defaults")
            }
        }
    }
}

@Composable
private fun ControlSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    unit: String = "",
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text(
                "${value.toInt()}$unit",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
