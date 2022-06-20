package com.biotech.framework.util

class EncodingUtil {
    //TODO Byte之間的轉換
    fun toBytes(s: String): ByteArray {
        return s.toByteArray()
    }

    fun toUTF8Bytes(s: String): ByteArray? {
        try {
            return s.toByteArray(charset("UTF-8"))
        } catch (e: Exception) {

        }

        return null
    }

    fun toBig5Bytes(s: String): ByteArray? {
        try {
            return s.toByteArray(charset("BIG5"))
        } catch (e: Exception) {

        }

        return null
    }

    fun toByteCollection(bytes: ByteArray?): Collection<Byte> {
        val c = arrayListOf<Byte>()
        if (bytes != null) {
            for (b in bytes) {
                c.add(b)
            }
        }
        return c
    }
}