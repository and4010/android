package com.biotech.framework.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.biotech.framework.extension.AsyncTask
import com.biotech.framework.interfaces.OnBluetoothClientListener
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class BluetoothClient(val bluetoothManager : BluetoothManager, socket : BluetoothSocket? = null) {

    private val TAG: String = this.javaClass.simpleName
    private var bluetoothSocket: BluetoothSocket? = null
    private var bluetoothInputStream: InputStream? = null
    private var bluetoothOutputStream: OutputStream? = null
    var isConnected = false
    var onBluetoothClientListener : OnBluetoothClientListener? = null

    init {
        socket?.let {
            bluetoothSocket = it
            bluetoothInputStream = it.inputStream
            bluetoothOutputStream = it.outputStream
            isConnected = it.isConnected
        }
    }

    fun connect(address : String) {
        bluetoothManager.getRemoteDeviceByAddress(address)?.let {device ->
            connect(device)
        }
    }


    fun connect(device: BluetoothDevice) {

        if(isConnected) return

        AsyncTask({
            if(!isConnected) {
                try {
                    // Cancel discovery because it otherwise slows down the connection.
                    bluetoothManager.cancelDiscovery()
                    // Connect the device through the socket. This will block
                    // until it succeeds or throws an exception
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(BluetoothManager.uuid)
                    bluetoothSocket?.let { socket ->
                        socket.connect()
                        isConnected = socket.isConnected

                    }

                } catch (connectException: IOException) {
                    // Unable to connect; close the socket and get out
                    disconnect()
                }
            }


        }, {
            bluetoothSocket?.let { socket ->
                if (isConnected) {
                    bluetoothInputStream = socket.inputStream
                    bluetoothOutputStream = socket.outputStream

                    onBluetoothClientListener?.onConnect(this)
                }
            }

        }, {
            disconnect()
        })
    }

    fun send(data : ByteArray, callback : () -> Unit) {
        AsyncTask({
            if(data.isNotEmpty()) {
                write(data)
            }
        }, {
            callback.invoke()
        }, {})
    }

    fun receive(callback: (byteArray : ByteArray?) -> Unit) {

        AsyncTask({
            Thread.sleep(1000)
            callback.invoke(read())
        }, {

        }, {
            callback.invoke(null)

        })
    }

    private fun write(byteArray: ByteArray) : Boolean {
        try {
            if (isConnected) {
                bluetoothOutputStream?.run {
                    write(byteArray)
                    flush()
                    return@write true
                }
            }
        } catch (e : IOException) {
            Log.e(TAG, "Output stream was disconnected",e)
        }
        return false
    }

    private fun read() : ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            // Read from the InputStream.
            try {

                if (bluetoothInputStream?.available() ?: 0 <= 0) {
                    break
                }
                val buffer = ByteArray(1024 * 5) // mmBuffer store for the stream

                val size = bluetoothInputStream?.read(buffer) ?: 0
                if (size > 0) {
                    byteArrayOutputStream.write(buffer.copyOfRange(0, size))
                }

            } catch (e: IOException) {
                Log.d(TAG, "Input stream was disconnected", e)
                break
            }
        }
        return byteArrayOutputStream.toByteArray()
    }

    fun disconnect() {

        if (bluetoothInputStream != null) {
            try {
                bluetoothInputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            bluetoothInputStream = null
        }

        if (bluetoothOutputStream != null) {
            try {
                bluetoothOutputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            bluetoothOutputStream = null
        }

        closeSocket()

        isConnected = false

    }

    private fun closeSocket() {

            try {
                bluetoothSocket?.let {
                    it.close()
                    onBluetoothClientListener?.onDisconnect(this)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            bluetoothSocket = null

    }

}