package com.biotech.framework.extension

import java.nio.ByteBuffer


fun longToBytes(x: Long): ByteArray {
    val buffer = ByteBuffer.allocate(java.lang.Long.BYTES)
    buffer.putLong(x)
    return buffer.array().copyOf(8)
}


fun bytesToLong(bytes: ByteArray): Long {
    val buffer = ByteBuffer.allocate(8)
    buffer.put(bytes, 0, bytes.size)
    buffer.flip()//need flip
    return buffer.long

}