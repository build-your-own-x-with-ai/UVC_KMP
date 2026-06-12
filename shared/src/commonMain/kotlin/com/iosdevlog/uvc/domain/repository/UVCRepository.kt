package com.iosdevlog.uvc.domain.repository

import com.iosdevlog.uvc.domain.model.*
import kotlinx.coroutines.flow.Flow

interface UVCRepository {
    fun getDevices(): Flow<List<UVCDevice>>
    suspend fun connect(deviceId: String): Result<Unit>
    suspend fun disconnect(deviceId: String)
    fun getVideoStream(deviceId: String): Flow<VideoFrame>
    fun getPacketLog(deviceId: String): Flow<USBPacket>
    suspend fun setControl(deviceId: String, control: CameraControl): Result<Unit>
    suspend fun getControlCapabilities(deviceId: String): Result<ControlCapabilities>
    suspend fun getActiveFormat(deviceId: String): Result<VideoFormat>
}

expect fun createUVCRepository(): UVCRepository
