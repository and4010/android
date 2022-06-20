package com.user.example.carton


import com.biotech.framework.barcode.GS128
import com.biotech.framework.util.DateUtil
import com.user.example.StartApplication
import com.user.example.database.entity.BarcodeRule
import java.math.BigDecimal
import java.util.*

open class CustomMeatCarton(rawData: String, selectedRule: BarcodeRule?) : Carton(rawData, selectedRule) {

    lateinit var barcodeDatas : Map<MeatBarcodeData, String>
    override val gtin by lazy { getData(MeatBarcodeData.ItemNo) }
    override val productionDate : Date? by lazy { getDate(rule?.ProductionDateFormat) }
    override val weight by lazy { convertWeight(rule?.WeightDecimalPoint) }
    override val weightUnit : GS128.WeightUnit by lazy { convertWeightUnit(rule?.WeightUnit) }
    override val barcodeRuleId : Int? by lazy {rule?.id}
    override val name : String? by lazy {rule?.Name}
    private var rule : BarcodeRule? = null

    enum class MeatBarcodeData{
        ItemNo,
        Weight,
        ProductionDate
    }

    override fun decode() {
        if (selectedRule != null){
            rule = selectedRule
            barcodeDatas = parse(rawData, rule)
        }else{
            autoGetBarcodeRules(rawData).let {
                when (it.size) {
                    0 -> {
                        rule = null
                        barcodeRuleList = null
                        barcodeDatas = parse(rawData, rule)
                    }
                    1 -> {
                        barcodeRuleList = it
                        rule = it.get(0)
                        barcodeDatas = parse(rawData, rule)
                    }
                    else -> {
                        barcodeRuleList = it
                        barcodeDatas = mutableMapOf()
                    }
                }
            }
        }
    }

    fun autoGetBarcodeRules(rawData: String) : List<BarcodeRule>{
        return StartApplication.instance.db.BarcodeRulesDao().getBarcodeRuleList(rawData, rawData.length)
    }


    private fun convertWeightUnit(weightUnit: Int?) : GS128.WeightUnit {
        var unit = GS128.WeightUnit.UNKNOWN
        try{
            unit = when (weightUnit){
                GS128.WeightUnit.KG.ordinal -> GS128.WeightUnit.KG
                GS128.WeightUnit.LB.ordinal -> GS128.WeightUnit.LB
                else -> GS128.WeightUnit.UNKNOWN
            }
        }catch (ex: Exception){

        }
        return unit
    }

    private fun convertWeight(point : Int?): Double {
        var weight = 0.0
        if (point == null) return weight
        try {
            weight = java.lang.Double.parseDouble(getData(MeatBarcodeData.Weight))
            val unscaled = BigDecimal(weight)
            val scaled = unscaled.scaleByPowerOfTen(-1 * point)
            weight = scaled.toDouble()
        } catch (ex: Exception) {

        }
        return weight
    }

    private fun getDate(format : String?) : Date? {
        var date : Date? = null
        try {
            date = if (getData(MeatBarcodeData.ProductionDate).isNotEmpty() && format != null)
                DateUtil.getDate(getData(MeatBarcodeData.ProductionDate), format)
            else null
        } catch (e: Exception) {

        }
        return date
    }

    fun getData(data : MeatBarcodeData) : String = barcodeDatas[data]?: ""


    fun parse(raw : String, rule: BarcodeRule?): Map<MeatBarcodeData, String> {

        val result = HashMap<MeatBarcodeData, String>()

        if (rule == null || rule.FullLength != raw.length){
            return result
        }

        result[MeatBarcodeData.ItemNo] = getItemNo(raw, rule)
        result[MeatBarcodeData.Weight] = getWeight(raw, rule)
        result[MeatBarcodeData.ProductionDate] = getProductionDate(raw, rule)

        return result
    }

    private fun getItemNo(raw : String, rule: BarcodeRule): String{
        try{
            return raw.substring(rule.ItemNoStartIndex - 1, rule.ItemNoStartIndex - 1 + rule.ItemNoLength)
//            if (rule.FixItemNo.isEmpty()){
//                return raw.substring(rule.ItemNoStartIndex - 1, rule.ItemNoStartIndex - 1 + rule.ItemNoLength)
//            }else{
//                return rule.FixItemNo
//            }
        }catch (e: java.lang.Exception){
            return ""
        }
    }

    private fun getWeight(raw : String, rule: BarcodeRule): String{
        try{
            return raw.substring(rule.WeightStartIndex - 1, rule.WeightStartIndex -1 + rule.WeightLength)
        }catch (e: java.lang.Exception){
            return ""
        }
    }

    private fun getProductionDate(raw : String, rule: BarcodeRule): String{
        try{
            return raw.substring(rule.ProductionDateStartIndex - 1, rule.ProductionDateStartIndex - 1 + rule.ProductionDateLength)
        }catch (e: java.lang.Exception){
            return ""
        }
    }
}