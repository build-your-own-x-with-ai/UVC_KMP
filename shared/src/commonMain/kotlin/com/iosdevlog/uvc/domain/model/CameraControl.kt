package com.iosdevlog.uvc.domain.model

data class CameraControl(
    val exposure: Int? = null,
    val focus: Int? = null,
    val brightness: Int? = null,
    val contrast: Int? = null,
    val whiteBalance: Int? = null,
    val autoExposure: Boolean = true,
    val autoFocus: Boolean = true,
    val autoWhiteBalance: Boolean = true
)

data class ControlRange(
    val min: Int,
    val max: Int,
    val step: Int,
    val default: Int
)

data class ControlCapabilities(
    val exposureRange: ControlRange? = null,
    val focusRange: ControlRange? = null,
    val brightnessRange: ControlRange? = null,
    val contrastRange: ControlRange? = null,
    val whiteBalanceRange: ControlRange? = null,
    val supportsAutoExposure: Boolean = false,
    val supportsAutoFocus: Boolean = false,
    val supportsAutoWhiteBalance: Boolean = false
)
