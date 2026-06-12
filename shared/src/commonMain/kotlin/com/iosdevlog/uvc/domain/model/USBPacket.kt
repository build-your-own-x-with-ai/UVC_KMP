package com.iosdevlog.uvc.domain.model

data class USBPacket(
    val timestamp: Long,
    val direction: Direction,
    val endpoint: Int,
    val length: Int,
    val data: ByteArray,
    val status: String = "OK"
) {
    enum class Direction {
        IN, OUT
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as USBPacket
        if (timestamp != other.timestamp) return false
        if (direction != other.direction) return false
        if (endpoint != other.endpoint) return false
        if (length != other.length) return false
        if (!data.contentEquals(other.data)) return false
        if (status != other.status) return false
        return true
    }

    override fun hashCode(): Int {
        var result = timestamp.hashCode()
        result = 31 * result + direction.hashCode()
        result = 31 * result + endpoint
        result = 31 * result + length
        result = 31 * result + data.contentHashCode()
        result = 31 * result + status.hashCode()
        return result
    }
}
