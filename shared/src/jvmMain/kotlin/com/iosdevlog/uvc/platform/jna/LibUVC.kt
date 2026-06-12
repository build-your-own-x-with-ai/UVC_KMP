package com.iosdevlog.uvc.platform.jna

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.ptr.PointerByReference

interface LibUVC : Library {
    companion object {
        val INSTANCE: LibUVC = Native.load("uvc", LibUVC::class.java)
        const val UVC_FRAME_FORMAT_MJPEG = 6
        const val UVC_FRAME_FORMAT_H264 = 13
        const val UVC_FRAME_FORMAT_YUYV = 4
    }

    fun uvc_init(context: PointerByReference, usb_ctx: Pointer?): Int
    fun uvc_exit(context: Pointer?)
    fun uvc_find_device(context: Pointer, devh: PointerByReference, vid: Int, pid: Int, serial: String?): Int
    fun uvc_open(device: Pointer, devh: PointerByReference): Int
    fun uvc_close(devh: Pointer)
    fun uvc_get_stream_ctrl_format_size(
        devh: Pointer, ctrl: Pointer, format: Int, width: Int, height: Int, fps: Int
    ): Int
    fun uvc_start_streaming(devh: Pointer, ctrl: Pointer, cb: Pointer?, user_ptr: Pointer?, flags: Int): Int
    fun uvc_stop_streaming(devh: Pointer)
}
