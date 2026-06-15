package com.iosdevlog.uvc.platform

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

actual class VideoRecorder {
    private var fileStream: FileOutputStream? = null
    private var isRecording = false
    private var currentFile: File? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())

    actual fun startRecording(): Result<String> = runCatching {
        if (isRecording) throw Exception("Already recording")

        val timestamp = dateFormat.format(Date())
        val filename = "UVC_Recording_$timestamp.h264"

        // Use app-specific external storage (doesn't require permissions)
        val moviesDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "UVC")
        if (!moviesDir.exists()) {
            moviesDir.mkdirs()
        }

        currentFile = File(moviesDir, filename)
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

        path
    }

    actual fun isRecording() = isRecording
}
