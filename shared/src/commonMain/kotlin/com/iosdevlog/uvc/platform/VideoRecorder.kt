package com.iosdevlog.uvc.platform

expect class VideoRecorder() {
    fun startRecording(): Result<String>
    fun writeFrame(data: ByteArray)
    fun stopRecording(): Result<String>
    fun isRecording(): Boolean
}
