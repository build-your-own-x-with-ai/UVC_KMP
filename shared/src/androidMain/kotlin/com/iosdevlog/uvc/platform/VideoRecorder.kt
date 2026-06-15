package com.iosdevlog.uvc.platform

actual class VideoRecorder {
    actual fun startRecording(): Result<String> {
        return Result.failure(Exception("Not implemented on Android"))
    }

    actual fun writeFrame(data: ByteArray) {
        // Not implemented
    }

    actual fun stopRecording(): Result<String> {
        return Result.failure(Exception("Not implemented on Android"))
    }

    actual fun isRecording(): Boolean = false
}
