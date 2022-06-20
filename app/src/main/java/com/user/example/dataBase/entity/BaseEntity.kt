package com.user.example.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class BaseEntity {
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}