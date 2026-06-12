package com.iosdevlog.uvc.platform

import com.iosdevlog.uvc.domain.model.UVCDevice
import com.iosdevlog.uvc.domain.model.VideoFormat
import com.iosdevlog.uvc.platform.jna.DeviceDescriptor
import com.iosdevlog.uvc.platform.jna.LibUSB
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.ptr.PointerByReference

class LibUSBManager {
    private val libusb = LibUSB.INSTANCE
    private var context: Pointer? = null

    fun init(): Result<Unit> = runCatching {
        val ctx = PointerByReference()
        val result = libusb.libusb_init(ctx)
        if (result != 0) throw Exception("libusb_init failed: $result")
        context = ctx.value
    }

    fun getDevices(): List<UVCDevice> {
        if (context == null) return emptyList()

        val devices = mutableListOf<UVCDevice>()
        val listRef = PointerByReference()
        val count = libusb.libusb_get_device_list(context, listRef)

        if (count < 0) return emptyList()

        val list = listRef.value
        for (i in 0 until count.toInt()) {
            val devicePtr = list.getPointer((i * Native.POINTER_SIZE).toLong())
            if (devicePtr != null) {
                val desc = DeviceDescriptor()
                if (libusb.libusb_get_device_descriptor(devicePtr, desc) == 0) {
                    if (desc.bDeviceClass == 14.toByte() || desc.bDeviceClass == 239.toByte()) {
                        val vendorId = desc.idVendor.toInt() and 0xFFFF
                        val productId = desc.idProduct.toInt() and 0xFFFF
                        val busNum = libusb.libusb_get_bus_number(devicePtr)
                        val devAddr = libusb.libusb_get_device_address(devicePtr)

                        devices.add(
                            UVCDevice(
                                id = "$busNum-$devAddr",
                                name = "UVC Camera ${vendorId.toString(16)}:${productId.toString(16)}",
                                vendorId = vendorId,
                                productId = productId,
                                serialNumber = null,
                                supportedFormats = listOf(VideoFormat.H264, VideoFormat.MJPEG, VideoFormat.YUV)
                            )
                        )
                    }
                }
            }
        }

        libusb.libusb_free_device_list(list, 1)
        return devices
    }

    fun close() {
        context?.let { libusb.libusb_exit(it) }
        context = null
    }
}
