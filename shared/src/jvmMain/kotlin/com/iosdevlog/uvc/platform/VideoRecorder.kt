package com.iosdevlog.uvc.platform

import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

actual class VideoRecorder {
    private var fileStream: FileOutputStream? = null
    private var isRecording = false
    private var currentFile: File? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")

    actual fun startRecording(): Result<String> = runCatching {
        if (isRecording) throw Exception("Already recording")

        val timestamp = dateFormat.format(Date())
        val filename = "UVC_Recording_$timestamp.h264"
        val desktopPath = System.getProperty("user.home") + "/Desktop"
        currentFile = File(desktopPath, filename)

        fileStream = FileOutputStream(currentFile!!)
        isRecording = true

        println("Recording started: ${currentFile!!.absolutePath}")
        currentFile!!.absolutePath
    }

    actual fun writeFrame(data: ByteArray) {
        if (isRecording) {
            fileStream?.write(data)
        }
    }

    actual fun stopRecording(): Result<String> = runCatching {
        if (!isRecording) throw Exception("Not recording")

        fileStream?.close()
        fileStream = null
        isRecording = false

        val path = currentFile?.absolutePath ?: ""
        println("Recording stopped: $path")
        println("Convert to MP4: ffmpeg -i \"$path\" -c copy \"${path.replace(".h264", ".mp4")}\"")

        path
    }

    actual fun isRecording() = isRecording
}
