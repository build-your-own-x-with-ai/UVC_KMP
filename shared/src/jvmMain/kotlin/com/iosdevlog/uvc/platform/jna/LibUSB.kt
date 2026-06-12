package com.iosdevlog.uvc.platform.jna

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.ptr.PointerByReference

interface LibUSB : Library {
    companion object {
        val INSTANCE: LibUSB = Native.load("usb-1.0", LibUSB::class.java)
    }

    fun libusb_init(context: PointerByReference?): Int
    fun libusb_exit(context: Pointer?)
    fun libusb_get_device_list(context: Pointer?, list: PointerByReference): Long
    fun libusb_free_device_list(list: Pointer?, unref: Int)
    fun libusb_get_device_descriptor(device: Pointer, desc: DeviceDescriptor): Int
    fun libusb_get_bus_number(device: Pointer): Int
    fun libusb_get_device_address(device: Pointer): Int
}
