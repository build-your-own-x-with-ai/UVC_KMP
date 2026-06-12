package com.iosdevlog.uvc.platform

object UVCNative {
    init {
        System.loadLibrary("uvc_jni")
    }

    external fun getVersion(): String
}
