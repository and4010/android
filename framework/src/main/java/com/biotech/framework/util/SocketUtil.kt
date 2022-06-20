package com.biotech.framework.util

import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class SocketUtil {
    interface SocketListener {
        fun onError(e: Exception?)
        fun onTimeout()
        fun DataReceived(data: ByteArray?)
    }

    var socketListener: SocketListener? = null
    var socket: Socket? = null
    var ipAddress: String? = null
    var port = 23000
    var timeout = 1000
    fun connect() {
        try {
            if (socket == null) socket = Socket()
            if (!socket!!.isConnected) {
                socket!!.connect(InetSocketAddress(ipAddress, port), timeout)
            }
        } catch (e: IOException) {
            if (socketListener != null) {
                socketListener!!.onError(e)
            }
            Log.e("SocketUtil", e.message!!)
        }
    }

    fun send(data: ByteArray?) {
        try {
            if (!socket!!.isConnected) {
                return
            }
            val bufferedOutputStream = BufferedOutputStream(socket!!.getOutputStream())
            bufferedOutputStream.write(data)
            bufferedOutputStream.flush()
        } catch (e: Exception) {
            if (socketListener != null) {
                socketListener!!.onError(e)
            }
        }
    }

    // 接收bytes
    fun waitForResponse(): ByteArray? {
        return try {
            if (!socket!!.isConnected) {
                return null
            }

            //接收
            val bufferedInputStream = BufferedInputStream(socket!!.getInputStream())
            val bytes = ByteArray(1024 * 5)
            var readLength: Int
            val byteArrayOutputStream = ByteArrayOutputStream()
            while (bufferedInputStream.read(bytes, 0, bytes.size).also { readLength = it } != -1) {
                byteArrayOutputStream.write(bytes, 0, readLength)
            }
            val response = byteArrayOutputStream.toByteArray()
            byteArrayOutputStream.close()
            response
        } catch (e: IOException) {
            null
        }
    }

    // 切斷服務端
    @Throws(IOException::class)
    fun close() {
        try {
            if (socket!!.channel != null) {
                socket!!.shutdownInput()
                socket!!.shutdownOutput()
                socket!!.getInputStream().close()
                socket!!.getOutputStream().close()
                socket!!.channel.close()
            }
        } catch (e: IOException) {
            if (socketListener != null) {
                socketListener!!.onError(e)
            }
        }
        socket!!.close()
    }
}