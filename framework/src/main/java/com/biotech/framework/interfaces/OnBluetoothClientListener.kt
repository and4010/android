package com.biotech.framework.interfaces

import com.biotech.framework.bluetooth.BluetoothClient

interface OnBluetoothClientListener {
    fun onConnect(client : BluetoothClient)
    fun onDisconnect(client : BluetoothClient)
}


