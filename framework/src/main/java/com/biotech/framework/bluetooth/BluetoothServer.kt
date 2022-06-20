package com.biotech.framework.bluetooth

import android.bluetooth.BluetoothServerSocket
import com.biotech.framework.extension.AsyncTask
import com.biotech.framework.interfaces.OnBluetoothServerListener
import java.io.IOException

class BluetoothServer(val bluetoothManager: BluetoothManager, socket: BluetoothServerSocket? = null) {

    private val TAG: String = this.javaClass.simpleName
    private var serverSocket : BluetoothServerSocket? = null
    var onBluetoothServerListener : OnBluetoothServerListener? = null
    var timeout : Int = 120
    private val msUnit : Int = 1000


    init {
        try {
            socket?.let {
                serverSocket = socket
            }

        }catch (e : IOException) {
        }
    }


    fun accept(discoverable : Boolean = false) {
        AsyncTask({

            try {
                // Cancel discovery because it otherwise slows down the connection.
                bluetoothManager.cancelDiscovery()
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                serverSocket?.let {
                    if(discoverable) {
                        bluetoothManager.enableDiscoverable(timeout)
                    }
                    val socket = it.accept(timeout * msUnit)
                    if(socket != null) {
                        BluetoothClient(bluetoothManager, socket).let {client ->
                            onBluetoothServerListener?.onClientConnect(client)
                        }
                    }
                }
            } catch (connectException: IOException) {
                // Unable to connect; close the socket and get out
                close()
            }

        }, {
        }, {
            close()
        })
    }


    fun close() {
        AsyncTask({

            try {

                serverSocket?.close()
                serverSocket = null

            } catch (connectException: IOException) {

            }

        }, {

        }, {
        })
    }

}