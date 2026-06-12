package com.iosdevlog.uvc.domain.repository

import com.iosdevlog.uvc.domain.model.*
import com.iosdevlog.uvc.platform.LibUSBManager
import kotlinx.coroutines.flow.*

class UVCRepositoryJvm : UVCRepository {
    private val usbManager = LibUSBManager()
    private val _devices = MutableStateFlow<List<UVCDevice>>(emptyList())

    init {
        usbManager.init()
        refreshDevices()
    }

    private fun refreshDevices() {
        _devices.value = usbManager.getDevices()
    }

    override fun getDevices(): Flow<List<UVCDevice>> = _devices.asStateFlow()

    override suspend fun connect(deviceId: String): Result<Unit> = Result.success(Unit)

    override suspend fun disconnect(deviceId: String) {
        refreshDevices()
    }

    override fun getVideoStream(deviceId: String): Flow<VideoFrame> = flowOf()

    override fun getPacketLog(deviceId: String): Flow<USBPacket> = flowOf()

    override suspend fun setControl(deviceId: String, control: CameraControl): Result<Unit> =
        Result.success(Unit)

    override suspend fun getControlCapabilities(deviceId: String): Result<ControlCapabilities> =
        Result.success(ControlCapabilities())

    override suspend fun getActiveFormat(deviceId: String): Result<VideoFormat> =
        Result.success(VideoFormat.MJPEG)
}

actual fun createUVCRepository(): UVCRepository = UVCRepositoryJvm()
