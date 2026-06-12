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
        println("Connecting to device: ${device.name} (${device.vendorId}:${device.productId})")

        val openResult = streamManager.openCamera(device.vendorId, device.productId)
        if (openResult.isFailure) {
            val error = openResult.exceptionOrNull()?.message ?: ""
            println("Failed to open camera: $error")

            // If permission denied on macOS, open System Settings
            if (error.contains("-3") && System.getProperty("os.name").contains("Mac")) {
                try {
                    Runtime.getRuntime().exec("open x-apple.systempreferences:com.apple.preference.security?Privacy_Camera")
                } catch (e: Exception) {
                    println("Failed to open System Settings: ${e.message}")
                }
            }
            throw openResult.exceptionOrNull() ?: Exception("Failed to open camera")
        }
        println("Camera opened successfully")

        val streamResult = streamManager.startStreaming()
        if (streamResult.isFailure) {
            println("Failed to start streaming: ${streamResult.exceptionOrNull()?.message}")
            throw streamResult.exceptionOrNull() ?: Exception("Failed to start streaming")
        }

        activeStreams[deviceId] = streamManager.getFrames()
        println("Connected successfully, stream active")
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
