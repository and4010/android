package com.user.example.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {

    @Insert
    fun insert(vararg item:T)

    @Insert
    fun insert(items:List<T>)

    @Update
    fun update(vararg item:T)

    @Update
    fun update(items: List<T>)

    @Delete
    fun delete(vararg item: T)
}