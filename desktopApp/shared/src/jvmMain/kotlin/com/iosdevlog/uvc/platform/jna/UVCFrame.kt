package com.iosdevlog.uvc.platform.jna

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure

@Structure.FieldOrder("data", "data_bytes", "width", "height", "frame_format", "sequence")
class UVCFrame : Structure() {
    @JvmField var data: Pointer? = null
    @JvmField var data_bytes: Int = 0
    @JvmField var width: Int = 0
    @JvmField var height: Int = 0
    @JvmField var frame_format: Int = 0
    @JvmField var sequence: Int = 0
}

interface FrameCallback : Callback {
    fun invoke(frame: UVCFrame?, user_ptr: Pointer?)
}
