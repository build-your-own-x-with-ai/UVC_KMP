package com.iosdevlog.uvc.platform.jna

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.NativeLong

@Structure.FieldOrder(
    "data", "data_bytes", "width", "height", "frame_format", "step", "sequence",
    "capture_time_tv_sec", "capture_time_tv_usec",
    "capture_time_finished_tv_sec", "capture_time_finished_tv_nsec",
    "source", "library_owns_data", "metadata"
)
class UVCFrame : Structure {
    @JvmField var data: Pointer? = null
    @JvmField var data_bytes: NativeLong = NativeLong(0)  // size_t
    @JvmField var width: Int = 0
    @JvmField var height: Int = 0
    @JvmField var frame_format: Int = 0
    @JvmField var step: NativeLong = NativeLong(0)  // size_t
    @JvmField var sequence: Int = 0
    @JvmField var capture_time_tv_sec: NativeLong = NativeLong(0)
    @JvmField var capture_time_tv_usec: NativeLong = NativeLong(0)
    @JvmField var capture_time_finished_tv_sec: NativeLong = NativeLong(0)
    @JvmField var capture_time_finished_tv_nsec: NativeLong = NativeLong(0)
    @JvmField var source: Pointer? = null
    @JvmField var library_owns_data: Byte = 0
    @JvmField var metadata: Pointer? = null

    constructor() : super()
    constructor(p: Pointer) : super(p)
}

interface FrameCallback : Callback {
    fun invoke(frame: Pointer?, user_ptr: Pointer?)  // Use Pointer, not UVCFrame
}
