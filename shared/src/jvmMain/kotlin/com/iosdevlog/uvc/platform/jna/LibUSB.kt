package com.iosdevlog.uvc.platform.jna

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.ptr.PointerByReference
import java.io.File

interface LibUSB : Library {
    companion object {
        init {
            val libDir = File("desktopApp/libs/macos").absolutePath
            System.setProperty("jna.library.path", libDir)
            println("JNA library path set to: $libDir")
        }

        val INSTANCE: LibUSB by lazy {
            try {
                Native.load("usb-1.0", LibUSB::class.java)
            } catch (e: UnsatisfiedLinkError) {
                System.err.println("Failed to load libusb: ${e.message}")
                System.err.println("jna.library.path: ${System.getProperty("jna.library.path")}")
                throw e
            }
        }
    }

    fun libusb_init(context: PointerByReference?): Int
    fun libusb_exit(context: Pointer?)
    fun libusb_get_device_list(context: Pointer?, list: PointerByReference): Long
    fun libusb_free_device_list(list: Pointer?, unref: Int)
    fun libusb_get_device_descriptor(device: Pointer, desc: DeviceDescriptor): Int
    fun libusb_get_bus_number(device: Pointer): Int
    fun libusb_get_device_address(device: Pointer): Int
}
