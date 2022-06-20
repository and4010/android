package com.user.example.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.user.example.database.dao.BarcodeRulesDao
import com.user.example.database.dao.BluetoothDevicesDao
import com.user.example.database.entity.BarcodeRule
import com.user.example.database.entity.BluetoothDevices


@Database(entities = [BluetoothDevices::class, BarcodeRule::class],version = 1,exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    companion object {
        private var instance : AppDataBase? = null

        fun getInstance(app:Application , path: String) : AppDataBase {
            if (instance == null){
                    instance = Room.databaseBuilder(app.applicationContext, AppDataBase::class.java,path)
                        .allowMainThreadQueries()
//                        .addMigrations(Migration_1_2)
                        .setJournalMode(JournalMode.TRUNCATE)
                        .build()
            }
            return instance!!
        }

//        private val Migration_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//            }
//        }

    }


    fun newInstace(app:Application ,path: String) : AppDataBase {
        instance?.let {
            if (it.isOpen)
                it.close()
        }
        instance = null
        return getInstance(app, path)
    }


    abstract fun BluetoothDevicesDao(): BluetoothDevicesDao

    abstract fun BarcodeRulesDao(): BarcodeRulesDao

}