package com.iosdevlog.uvc.platform.jna

import com.sun.jna.Structure

@Structure.FieldOrder(
    "bLength", "bDescriptorType", "bcdUSB", "bDeviceClass", "bDeviceSubClass",
    "bDeviceProtocol", "bMaxPacketSize0", "idVendor", "idProduct", "bcdDevice",
    "iManufacturer", "iProduct", "iSerialNumber", "bNumConfigurations"
)
class DeviceDescriptor : Structure() {
    @JvmField var bLength: Byte = 0
    @JvmField var bDescriptorType: Byte = 0
    @JvmField var bcdUSB: Short = 0
    @JvmField var bDeviceClass: Byte = 0
    @JvmField var bDeviceSubClass: Byte = 0
    @JvmField var bDeviceProtocol: Byte = 0
    @JvmField var bMaxPacketSize0: Byte = 0
    @JvmField var idVendor: Short = 0
    @JvmField var idProduct: Short = 0
    @JvmField var bcdDevice: Short = 0
    @JvmField var iManufacturer: Byte = 0
    @JvmField var iProduct: Byte = 0
    @JvmField var iSerialNumber: Byte = 0
    @JvmField var bNumConfigurations: Byte = 0
}
