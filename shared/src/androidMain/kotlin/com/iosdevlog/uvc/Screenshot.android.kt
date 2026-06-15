package com.iosdevlog.uvc

import com.iosdevlog.uvc.domain.model.VideoFrame

actual suspend fun captureScreenshot(frame: VideoFrame): String {
    return "Not implemented on Android"
}
