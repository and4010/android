package com.biotech.framework.interfaces

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket

interface OnBluetoothNotifyListener {
    fun onDiscovery(device : BluetoothDevice)
    fun onStateChanged(device: BluetoothDevice)
}