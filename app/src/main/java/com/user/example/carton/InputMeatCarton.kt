package com.user.example.carton

import com.user.example.database.entity.BarcodeRule

class InputMeatCarton(rawData: String, decodeOrder: DecodeOrder, selectedRule: BarcodeRule?) {

    var carton: Carton?

    init {
        carton = start(rawData, decodeOrder, selectedRule)
    }

    enum class DecodeOrder{
        GS1First,
        CustomFirst
    }

    fun start(rawData: String, decodeOrder: DecodeOrder, selectedRule: BarcodeRule?) : Carton?{
        var tempCarton : Carton?
        if (selectedRule != null){ //已指定條碼規則
            tempCarton = CustomMeatCarton(rawData, selectedRule)
            return tempCarton
        }

        //未指定條碼規則
        when(decodeOrder){
            InputMeatCarton.DecodeOrder.GS1First -> {
                tempCarton = MeatCarton(rawData)
                if (!checkDecode(tempCarton)) {
                    tempCarton = CustomMeatCarton(rawData, selectedRule)
                    if (!checkDecode(tempCarton)){
                        tempCarton = null
                    }
                }
            }
            else ->{
                tempCarton = CustomMeatCarton(rawData, selectedRule)
                if (!checkDecode(tempCarton)){
                    tempCarton = MeatCarton(rawData)
                    if (!checkDecode(tempCarton)){
                        tempCarton = null
                    }
                }
            }
        }
        return tempCarton
    }

    fun checkDecode( carton: Carton) : Boolean{
        carton.barcodeRuleList?.let {
            if (it.count() > 1){
                return true
            }
        }
        if (carton.gtin.isEmpty() || carton.weight == 0.0){
//        if (carton.barcodeRuleId == null){
            return false
        }else{
            return true
        }
    }
}