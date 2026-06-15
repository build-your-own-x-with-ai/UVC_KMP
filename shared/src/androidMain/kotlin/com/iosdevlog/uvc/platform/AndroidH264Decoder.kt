package com.iosdevlog.uvc.platform

import android.content.Context
import android.media.MediaCodec
import android.media.MediaFormat
import android.view.Surface
import com.iosdevlog.uvc.domain.model.VideoFormat
import java.nio.ByteBuffer

class AndroidH264Decoder {
    private var mediaCodec: MediaCodec? = null
    private var surface: Surface? = null

    fun init(width: Int, height: Int, outputSurface: Surface) {
        surface = outputSurface

        val format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height)
        format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 1024 * 1024)
        format.setInteger(MediaFormat.KEY_MAX_WIDTH, width)
        format.setInteger(MediaFormat.KEY_MAX_HEIGHT, height)

        mediaCodec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC).apply {
            configure(format, surface, null, 0)
            start()
        }

        println("AndroidH264Decoder initialized: ${width}x${height}")
    }

    fun decode(data: ByteArray, timestamp: Long = System.nanoTime() / 1000): Boolean {
        val codec = mediaCodec ?: return false

        try {
            val inputBufferIndex = codec.dequeueInputBuffer(10000)
            if (inputBufferIndex >= 0) {
                val inputBuffer = codec.getInputBuffer(inputBufferIndex)
                inputBuffer?.clear()
                inputBuffer?.put(data)
                codec.queueInputBuffer(inputBufferIndex, 0, data.size, timestamp, 0)
            }

            val bufferInfo = MediaCodec.BufferInfo()
            val outputBufferIndex = codec.dequeueOutputBuffer(bufferInfo, 10000)
            if (outputBufferIndex >= 0) {
                codec.releaseOutputBuffer(outputBufferIndex, true)
                return true
            }
        } catch (e: Exception) {
            println("Decode error: ${e.message}")
        }

        return false
    }

    fun release() {
        mediaCodec?.stop()
        mediaCodec?.release()
        mediaCodec = null
    }
}
