package com.user.example.carton

import com.biotech.framework.util.DateUtil
import com.biotech.framework.barcode.GS128
import com.user.example.database.entity.BarcodeRule
import java.math.BigDecimal
import java.util.*

open class MeatCarton(rawData: String , selectedRule: BarcodeRule? = null) : Carton(rawData, selectedRule) {

    override lateinit var aIs : Map<String, String>
    override val gtin by lazy { getAI("01") }
    override val serialNumber by lazy { getAI("21") }
    override val batchLotNumber by lazy { getAI("10") }
    override val productionDate : Date? by lazy { getDate("11", "yyMMdd") }
    override val packingDate : Date? by lazy { getDate("13", "yyMMdd") }
    override val expiredDate : Date? by lazy { getDate("17", "yyMMdd") }
    override val harvestDate : Date? by lazy { getDate("7007", "yyMMdd") }
    override val weight by lazy { convertWeight() }
    override val name by lazy { "GS1" }
//    override val barcodeRuleId : Int? by lazy {rule?.id}
//    override val name : String? by lazy {rule?.Name}
//    var rule : BarcodeRule? = null

    override val weightUnit : GS128.WeightUnit by lazy {
        when {
            !aIs.keys.find { key -> key.contains("310") }.isNullOrEmpty() -> GS128.WeightUnit.KG
            !aIs.keys.find { key -> key.contains("320") }.isNullOrEmpty() -> GS128.WeightUnit.LB
            else -> GS128.WeightUnit.UNKNOWN
        }
    }

    override fun decode() {
        aIs = GS128().Parse(rawData)
//        getGS1BarcodeRules(getAI("01")).let {
//            when(it.size){
//                0 -> {
//                    rule = null
//                }
//                1 -> {
//                    rule = it.get(0)
//                }
//                else ->{
//                    //之後要改為給使用者選擇條碼規則
//                    rule = it.get(0)
//                }
//            }
//        }
    }

//    fun getGS1BarcodeRules(gtin: String) : List<BarcodeRule>{
//
//        return StartApplication.instance.db.BarcodeRulesDao().getBarcodeRuleList(gtin, true)
//    }

    private fun convertWeight(): Double {
        var weight = 0.0
        aIs.keys.find { key -> key.length == 4 && (key.contains("310") || key.contains("320")) }?.let {
            try {
                weight = java.lang.Double.parseDouble(getAI(it))
                val unscaled = BigDecimal(weight)
                val scaled = unscaled.scaleByPowerOfTen(-1 * it.substring(3,4).toInt())
                weight = scaled.toDouble()
            } catch (ex: Exception) {

            }
        }
        return weight
    }

    private fun getDate(ai: String, format : String) : Date? {
        var date : Date? = null
        try {
            date = if (getAI(ai).isNotEmpty()) DateUtil.getDate(getAI(ai), format) else null
        } catch (e: Exception) {

        }
        return date
    }

    fun getAI(ai : String) : String = aIs[ai]?: ""
}