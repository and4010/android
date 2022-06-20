package com.user.example.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.biotech.framework.model.KeyValueModel
import com.user.example.database.dao.BaseDao
import com.user.example.database.entity.BluetoothDevices

@Dao
interface BluetoothDevicesDao : BaseDao<BluetoothDevices> {


    @Query("select Name as [key],Mac as [value] from bluetooth_devices")
    fun getAll():LiveData<List<KeyValueModel>>

    @Query("DELETE FROM bluetooth_devices WHERE Name = :name")
    fun deleteByName(name :String)

    @Query("DELETE FROM bluetooth_devices WHERE mac = :mac")
    fun deleteByMac(mac :String)

    @Query("select Name as [key],Mac as [value]  from bluetooth_devices where mac = :mac")
    fun searchByMac(mac: String) : List<KeyValueModel>

    @Query("select Name as [key],Mac as [value]  from bluetooth_devices where Name = :name")
    fun searchByName(name: String) : List<KeyValueModel>

    @Query("SELECT * FROM bluetooth_devices WHERE Name = :name")
    fun getByName(name: String): BluetoothDevices


}