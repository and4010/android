package com.user.example.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.user.example.database.dao.BaseDao
import com.user.example.database.entity.BarcodeRule

@Dao
interface BarcodeRulesDao : BaseDao<BarcodeRule> {

    @Query("select * from barcode_rules")
    fun getAll(): List<BarcodeRule>

    @Query("select * from barcode_rules where ItemNo = substr(:barcode, ItemNoStartIndex, ItemNoLength) and FullLength = :length")
    fun getBarcodeRuleList(barcode : String, length : Int): List<BarcodeRule>

//    @Query("select * from barcode_rules where ItemNo = :itemNo and GS1 = :gs1")
//    fun getBarcodeRuleList(itemNo : String, gs1 : Boolean): List<BarcodeRule>
}