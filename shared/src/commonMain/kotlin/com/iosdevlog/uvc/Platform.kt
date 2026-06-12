package com.iosdevlog.uvc

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform