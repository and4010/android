package com.biotech.framework.interfaces

import com.biotech.framework.bluetooth.BluetoothClient

interface OnBluetoothServerListener {
    fun onClientConnect(client : BluetoothClient)
    fun onClientDisconnect(client : BluetoothClient)
}