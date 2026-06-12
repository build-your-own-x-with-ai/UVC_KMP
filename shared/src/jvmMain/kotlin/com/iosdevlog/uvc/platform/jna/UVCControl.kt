package com.iosdevlog.uvc.platform.jna

import com.sun.jna.Pointer

// UVC Control selectors
object UVCControl {
    // Camera Terminal Controls
    const val UVC_CT_EXPOSURE_TIME_ABSOLUTE_CONTROL = 0x04
    const val UVC_CT_FOCUS_ABSOLUTE_CONTROL = 0x06
    const val UVC_CT_ZOOM_ABSOLUTE_CONTROL = 0x0B

    // Processing Unit Controls
    const val UVC_PU_BRIGHTNESS_CONTROL = 0x02
    const val UVC_PU_CONTRAST_CONTROL = 0x03
    const val UVC_PU_SATURATION_CONTROL = 0x07
    const val UVC_PU_WHITE_BALANCE_TEMPERATURE_CONTROL = 0x0A
    const val UVC_PU_GAIN_CONTROL = 0x04
}

interface LibUVCExt {
    // Get control value
    fun uvc_get_ctrl(devh: Pointer, unit: Byte, selector: Byte, data: Pointer, len: Int, req_code: Byte): Int

    // Set control value
    fun uvc_set_ctrl(devh: Pointer, unit: Byte, selector: Byte, data: Pointer, len: Int): Int
}
