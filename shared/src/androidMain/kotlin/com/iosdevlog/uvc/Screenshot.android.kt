package com.iosdevlog.uvc

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.iosdevlog.uvc.domain.model.VideoFrame
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

private lateinit var appContext: Context

fun initScreenshot(context: Context) {
    appContext = context.applicationContext
}

actual suspend fun captureScreenshot(frame: VideoFrame): String {
    val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
    val filename = "UVC_Screenshot_$timestamp.png"

    // For Android 10+ use MediaStore
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/UVC")
        }

        val resolver = appContext.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            println("Screenshot saved to MediaStore: $uri")
            return uri.toString()
        }
    } else {
        // For older Android versions
        val picturesDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "UVC"
        )
        if (!picturesDir.exists()) {
            picturesDir.mkdirs()
        }

        val file = File(picturesDir, filename)
        println("Screenshot saved: ${file.absolutePath}")
        return file.absolutePath
    }

    throw Exception("Failed to save screenshot")
}
