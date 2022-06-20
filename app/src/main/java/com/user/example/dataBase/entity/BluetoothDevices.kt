package com.user.example.database.entity

import androidx.room.Entity
import com.user.example.database.entity.BaseEntity

@Entity(tableName = "bluetooth_devices")
data class BluetoothDevices (

    val Mac : String,
    var Name : String,
    var Type : Int
): BaseEntity()