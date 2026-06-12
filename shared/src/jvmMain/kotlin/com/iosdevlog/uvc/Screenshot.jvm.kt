package com.iosdevlog.uvc

import com.iosdevlog.uvc.domain.model.VideoFrame
import com.iosdevlog.uvc.platform.ScreenshotSaver

actual suspend fun captureScreenshot(frame: VideoFrame): String {
    return ScreenshotSaver.saveScreenshot(frame).getOrThrow()
}
