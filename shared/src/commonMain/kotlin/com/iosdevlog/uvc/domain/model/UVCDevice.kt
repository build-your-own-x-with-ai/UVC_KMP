package com.iosdevlog.uvc.domain.model

data class UVCDevice(
    val id: String,
    val name: String,
    val vendorId: Int,
    val productId: Int,
    val serialNumber: String?,
    val supportedFormats: List<VideoFormat>
)

enum class VideoFormat {
    MJPEG,
    H264,
    YUV,
    UNKNOWN
}
