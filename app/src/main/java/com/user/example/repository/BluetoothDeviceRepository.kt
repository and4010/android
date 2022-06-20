package com.user.example.repository

import android.app.Application
import com.user.example.database.AppDataBase
import com.user.example.database.dao.BluetoothDevicesDao
import com.user.example.database.entity.BluetoothDevices

class BluetoothDeviceRepository(application: Application, path : String) {

    val bluetoothDeviceDao : BluetoothDevicesDao by lazy { AppDataBase.getInstance(application, path).BluetoothDevicesDao() }

    fun getDevices() = bluetoothDeviceDao.getAll()

    fun exists(device : BluetoothDevices) : Boolean {
        val a = bluetoothDeviceDao.searchByMac(device.Mac)
        if(a.isNotEmpty()) {
            return true
        }
        val b = bluetoothDeviceDao.searchByName(device.Name)
        if(b.isNotEmpty()) {
            return true
        }

        return false
    }

    fun saveDevice(device: BluetoothDevices) {
        if (!exists(device)) {
            bluetoothDeviceDao.insert(device)
        }
    }

    fun deleteDevice(address: String) {

        val device = BluetoothDevices(address, "", 0)

        if(exists(device)) {
            bluetoothDeviceDao.deleteByMac(device.Mac)
        }
    }

}