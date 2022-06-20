package com.user.example.carton

import com.biotech.framework.barcode.GS128
import com.user.example.database.entity.BarcodeRule
import java.util.*

abstract class Carton(
    val rawData : String,
    val selectedRule: BarcodeRule?
) {
    open var barcodeRuleList : List<BarcodeRule>? = null
    open val barcodeRuleId : Int? = null
    open val name : String? = null
    open var aIs : Map<String, String> = hashMapOf()
    open val gtin = ""
    open val serialNumber = ""
    open val batchLotNumber = ""
    open val productionDate : Date? = null
    open val packingDate : Date? = null
    open val expiredDate : Date? = null
    open val harvestDate : Date? = null
    open val weight : Double = 0.0
    open val weightUnit : GS128.WeightUnit = GS128.WeightUnit.UNKNOWN

    init {
        decode()
    }

    abstract fun decode()

}