package com.iosdevlog.uvc

import com.iosdevlog.uvc.domain.model.VideoFrame

expect suspend fun captureScreenshot(frame: VideoFrame): String
