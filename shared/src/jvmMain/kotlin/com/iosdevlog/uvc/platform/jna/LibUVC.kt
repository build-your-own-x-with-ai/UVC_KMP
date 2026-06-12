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
    fun uvc_get_device_list(context: Pointer, list: PointerByReference): Int
    fun uvc_free_device_list(list: Pointer, unref: Int)
    fun uvc_get_device_descriptor(device: Pointer, desc: PointerByReference): Int
    fun uvc_free_device_descriptor(desc: Pointer)
    fun uvc_open(device: Pointer, devh: PointerByReference): Int
    fun uvc_close(devh: Pointer)
    fun uvc_start_streaming(devh: Pointer, ctrl: Pointer, cb: Pointer?, user_ptr: Pointer?, flags: Int): Int
    fun uvc_stop_streaming(devh: Pointer)
}
