package com.iosdevlog.uvc.platform

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.iosdevlog.uvc.domain.model.UVCDevice
import com.iosdevlog.uvc.domain.model.VideoFormat

class AndroidUSBManager(private val context: Context) {
    private val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager

    fun getDevices(): List<UVCDevice> {
        val devices = mutableListOf<UVCDevice>()

        for (device in usbManager.deviceList.values) {
            // Check if it's a UVC device (Video class)
            if (isUVCDevice(device)) {
                devices.add(
                    UVCDevice(
                        id = device.deviceId.toString(),
                        name = device.deviceName ?: "UVC Camera",
                        vendorId = device.vendorId,
                        productId = device.productId,
                        serialNumber = device.serialNumber,
                        supportedFormats = listOf(VideoFormat.H264, VideoFormat.MJPEG, VideoFormat.YUV)
                    )
                )
            }
        }

        return devices
    }

    private fun isUVCDevice(device: UsbDevice): Boolean {
        // UVC device class = 14 (Video)
        if (device.deviceClass == 14) return true

        // Check interfaces for video class
        for (i in 0 until device.interfaceCount) {
            val intf = device.getInterface(i)
            if (intf.interfaceClass == 14) return true
        }

        return false
    }

    fun getDevice(deviceId: String): UsbDevice? {
        return usbManager.deviceList.values.find { it.deviceId.toString() == deviceId }
    }

    fun hasPermission(device: UsbDevice): Boolean {
        return usbManager.hasPermission(device)
    }

    fun requestPermission(device: UsbDevice, callback: (Boolean) -> Unit) {
        if (hasPermission(device)) {
            callback(true)
        } else {
            // TODO: Request permission with PendingIntent
            callback(false)
        }
    }
}
