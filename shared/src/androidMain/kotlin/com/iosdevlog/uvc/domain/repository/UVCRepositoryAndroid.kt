package com.iosdevlog.uvc.domain.repository

import com.iosdevlog.uvc.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UVCRepositoryAndroid : UVCRepository {
    override fun getDevices(): Flow<List<UVCDevice>> = flowOf(emptyList())

    override suspend fun connect(deviceId: String): Result<Unit> = Result.success(Unit)

    override suspend fun disconnect(deviceId: String) {}

    override fun getVideoStream(deviceId: String): Flow<VideoFrame> = flowOf()

    override fun getPacketLog(deviceId: String): Flow<USBPacket> = flowOf()

    override suspend fun setControl(deviceId: String, control: CameraControl): Result<Unit> = Result.success(Unit)

    override suspend fun getControlCapabilities(deviceId: String): Result<ControlCapabilities> =
        Result.success(ControlCapabilities())

    override suspend fun getActiveFormat(deviceId: String): Result<VideoFormat> =
        Result.success(VideoFormat.UNKNOWN)
}

actual fun createUVCRepository(): UVCRepository = UVCRepositoryAndroid()
