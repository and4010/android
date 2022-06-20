package com.user.example.model

import com.biotech.framework.adapter.RecycleViewKeyValueAdapter
import com.biotech.framework.model.KeyValueModel
import com.user.example.database.entity.BarcodeRule

class BarcodeRuleModel {


    var barcodeRuleArray : Array<BarcodeRule>? = null

    fun insertdata():MutableList<KeyValueModel>{
        val list = mutableListOf<KeyValueModel>()
        barcodeRuleArray!!.forEach { map ->
            list.add(
                KeyValueModel(
                    map.Name, map.ItemNo
                )
            )
        }
        return list
    }


    fun getSelectedBarcodeRule(recycleViewKeyValueAdapter: RecycleViewKeyValueAdapter) : BarcodeRule?{
        recycleViewKeyValueAdapter.getSelectedPosition().let {
            return if(it == -1){
                null
            }else{
                barcodeRuleArray?.get(it)
            }
        }
    }

    fun check(recycleViewKeyValueAdapter: RecycleViewKeyValueAdapter): BarcodeRule?{
        var barcode : BarcodeRule? = null
        getSelectedBarcodeRule(recycleViewKeyValueAdapter).let {
            if (it == null){
                return@let
            }else{
                barcode = getSelectedBarcodeRule(recycleViewKeyValueAdapter)!!
            }
        }
        return barcode
    }

}