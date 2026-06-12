package com.iosdevlog.uvc.domain.repository

import com.iosdevlog.uvc.domain.model.*
import com.iosdevlog.uvc.platform.LibUSBManager
import com.iosdevlog.uvc.platform.UVCStreamManager
import kotlinx.coroutines.flow.*

class UVCRepositoryJvm : UVCRepository {
    private val usbManager = LibUSBManager()
    private val streamManager = UVCStreamManager()
    private val _devices = MutableStateFlow<List<UVCDevice>>(emptyList())
    private val activeStreams = mutableMapOf<String, Flow<VideoFrame>>()

    init {
        val result = usbManager.init()
        if (result.isSuccess) {
            refreshDevices()
        } else {
            System.err.println("Failed to initialize USB: ${result.exceptionOrNull()}")
        }
        streamManager.init()
    }

    private fun refreshDevices() {
        _devices.value = usbManager.getDevices()
    }

    override fun getDevices(): Flow<List<UVCDevice>> = _devices.asStateFlow()

    override suspend fun connect(deviceId: String): Result<Unit> = runCatching {
        val device = _devices.value.find { it.id == deviceId } ?: throw Exception("Device not found")
        streamManager.openCamera(device.vendorId, device.productId).getOrThrow()
        streamManager.startStreaming().getOrThrow()
        activeStreams[deviceId] = streamManager.getFrames()
    }

    override suspend fun disconnect(deviceId: String) {
        streamManager.stopStreaming()
        activeStreams.remove(deviceId)
        refreshDevices()
    }

    override fun getVideoStream(deviceId: String): Flow<VideoFrame> =
        activeStreams[deviceId] ?: flowOf()

    override fun getPacketLog(deviceId: String): Flow<USBPacket> = flowOf()

    override suspend fun setControl(deviceId: String, control: CameraControl): Result<Unit> =
        Result.success(Unit)

    override suspend fun getControlCapabilities(deviceId: String): Result<ControlCapabilities> =
        Result.success(ControlCapabilities())

    override suspend fun getActiveFormat(deviceId: String): Result<VideoFormat> =
        Result.success(VideoFormat.MJPEG)
}

actual fun createUVCRepository(): UVCRepository = UVCRepositoryJvm()
