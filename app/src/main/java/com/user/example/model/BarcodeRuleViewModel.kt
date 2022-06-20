package com.user.example.model


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.biotech.framework.adapter.RecycleViewKeyValueAdapter
import com.biotech.framework.model.KeyValueModel
import com.user.example.database.entity.BarcodeRule


class BarcodeRuleViewModel : ViewModel() {


    val barcodeModel by lazy { BarcodeRuleModel() }
    private var BarCodeList = mutableListOf<KeyValueModel>()
    val recycleViewKeyValueAdapter by lazy { RecycleViewKeyValueAdapter(mutableListOf()) }
    var mListener : ((BarcodeRule?) -> Unit)? = null
    var cancel = MutableLiveData<Boolean>()
    var confirm = MutableLiveData<Boolean>()


    init {
    }

    fun setInsertdata(){
        BarCodeList = barcodeModel.insertdata()
        recycleViewKeyValueAdapter.addAll(BarCodeList)
        recycleViewKeyValueAdapter.notifyDataSetChanged()
    }

    fun confirm(){
        if (barcodeModel.check(recycleViewKeyValueAdapter) == null){
            confirm.value = false
        }else{
            confirm.value = true
            mListener?.invoke(barcodeModel.check(recycleViewKeyValueAdapter))
        }
    }

    fun Cancel(){
        cancel.value = true
    }

}