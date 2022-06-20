package com.user.example.model

import android.app.Application
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.user.example.carton.InputMeatCarton
import com.user.example.database.entity.BarcodeRule
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.biotech.framework.adapter.RecycleViewKeyValueAdapter
import com.biotech.framework.barcode.GS128
import com.biotech.framework.model.KeyValueModel
import com.biotech.framework.util.DateUtil
import com.biotech.framework.viewModel.BaseViewModel
import com.user.example.StartApplication

class MeatCartonDemoViewModel(application: Application) : BaseViewModel(application) {

    val meatCartonDemoModel by lazy { MeatCartonDemoModel() }
    val chkGS1fFirst = ObservableBoolean()
    val chkAutoFixRule = ObservableBoolean()
    var selectBarcodeRule = MutableLiveData<Boolean>()

    var observableText = ObservableField<String>() //test

    var btnCancelVisibility = ObservableBoolean()
    var chkGS1Visibility = ObservableBoolean()
    var chkAutoVisibility = ObservableBoolean()
    var dialogRule = MutableLiveData<Boolean>()
    var keyModelList = MutableLiveData<MutableList<KeyValueModel>>()


    init {
        chkGS1Visibility.set(false)
        chkAutoVisibility.set(false)
        btnCancelVisibility.set(true)
        syandardEditTextCallBack = { value ->  setbarcode(value,getDecodeMode()) }
        syandardButtonCallBack = { view ->  clear() }
    }

    private var selectedRule : BarcodeRule? = null
    private var cancelSelectedRule : Boolean = false
    val state by lazy { State(MutableLiveData(),MutableLiveData(),MutableLiveData(),MutableLiveData(),MutableLiveData(),
        MutableLiveData(),MutableLiveData(),MutableLiveData(),MutableLiveData()) }

    fun setbarcode(text:String,getDecodeMode: InputMeatCarton.DecodeOrder){
        clear()
        val carton = InputMeatCarton(text, getDecodeMode, selectedRule).carton ?: return
        if (cancelSelectedRule) {
            cancelSelectedRule = false
            return
        }
        val barcodeRuleCount = carton.barcodeRuleList?.count() ?: 0
        if (barcodeRuleCount >= 2) {
            ShowBarcodeRule(carton.barcodeRuleList!!.toTypedArray())
            return
        }

        if (barcodeRuleCount == 1 && selectedRule == null ){
            if (chkAutoFixRule.get()){
                fixBarcodeRule(carton.barcodeRuleList!![0])
                barcodeRuleViewInit()
            }else{
                dialogRule.value = true
                fixBarcodeRule(carton.barcodeRuleList!![0])
            }
        }
        state.txtName.value = carton.name ?: ""
        state.txtGtin.value = carton.gtin
        state.txtSerialNumber.value = carton.serialNumber
        state.txtBatchLotNumber.value = carton.batchLotNumber
        try {
            state.txtPackageDate.value = (
                    if (carton.packingDate != null) DateUtil.getString(
                        carton.packingDate!!,
                        DateUtil.DateEnum.DByHyphen
                    ) else ""
                )
            } catch (e: Exception) {
            state.txtPackageDate.value = ""
            }
            try {
                state.txtDeliveryDate.value =(
                    if (carton.productionDate != null) DateUtil.getString(
                        carton.productionDate!!,
                        DateUtil.DateEnum.DByHyphen
                    ) else ""
                )
            } catch (ex: Exception) {
                state.txtDeliveryDate.value = ""
            }
            try {
                state.txtExpiredDate.value =(
                    if (carton.expiredDate != null) DateUtil.getString(
                        carton.expiredDate!!,
                        DateUtil.DateEnum.DByHyphen
                    ) else ""
                )
            } catch (ex: Exception) {
                state.txtExpiredDate.value = ""
            }
            try {
                state.txtHarvestDate.value =(
                    if (carton.harvestDate != null) DateUtil.getString(
                        carton.harvestDate!!,
                        DateUtil.DateEnum.DByHyphen
                    ) else ""
                )
            } catch (ex: Exception) {
                state.txtHarvestDate.value = ""
            }
        state.txtWeight.value = (carton.weight.toString() + carton.weightUnit.name)
        val list = mutableListOf<KeyValueModel>()
        if (carton.aIs.isNotEmpty()) {
            val gs128 = GS128()
            carton.aIs.forEach { map ->
                list.add(
                    KeyValueModel(
                        map.key + " " + gs128.getDescription(
                            map.key
                        )?.Description, map.value
                    )
                )
            }
            keyModelList.value = list
        }
    }



    fun getDecodeMode() : InputMeatCarton.DecodeOrder{
        return if (chkGS1fFirst.get()){
            InputMeatCarton.DecodeOrder.GS1First
        }else{
            InputMeatCarton.DecodeOrder.CustomFirst
        }
    }

    fun setrule(){
        barcodeRuleViewInit()
    }

    fun onGs1CheckedChanged(v: View) {
        chkGS1fFirst.set((v as CheckBox).isChecked)
    }
    fun onAutoFixCheckedChanged(v: View) {
        chkAutoFixRule.set((v as CheckBox).isChecked)
    }
    fun selectBarcodeRule(){
        ShowBarcodeRule(getbarcodeRuleDb())
    }

    fun ShowBarcodeRule(barcodeRuleArray : Array<BarcodeRule>){
        selectBarcodeRule.value = true
    }

    fun setbarcodeRule(barcodeRule: BarcodeRule?){
        if (barcodeRule == null) {
            cancelSelectedRule = true
        }else {
            fixBarcodeRule(barcodeRule)
            cancelSelectedRule = false
        }
    }

    fun getbarcodeRuleDb():Array<BarcodeRule>{
       return StartApplication.instance.db.BarcodeRulesDao().getAll().toTypedArray()
    }

    fun cancaelRule(){
        cancelFixBarcodeRule()
    }

//    fun onTextChanged(s: CharSequence,start: Int,before : Int, count :Int){
//        setbarcode(s.toString(),getDecodeMode())
//    }

    private fun barcodeRuleViewInit(){
        if (selectedRule == null){
            state.txtName.value =""
            chkGS1Visibility.set(false)
            chkAutoVisibility.set(false)
            btnCancelVisibility.set(true)
        }else{
            chkGS1Visibility.set(true)
            chkAutoVisibility.set(true)
            btnCancelVisibility.set(false)
            selectedRule?.let {
                if (it.Name.isEmpty()){
                    state.txtName.value = ""
                }else{
                    state.txtName.value = it.Name
                }
            }
        }
    }





    private fun fixBarcodeRule(barcodeRule : BarcodeRule){
        selectedRule = barcodeRule
    }

    private fun cancelFixBarcodeRule(){
        selectedRule = null
        clear()
    }


    private fun clear() {
        barcodeRuleViewInit()
        state.txtGtin.value = ""
        state.txtSerialNumber.value = ""
        state.txtBatchLotNumber.value = ""
        state.txtPackageDate.value = ""
        state.txtDeliveryDate.value = ""
        state.txtExpiredDate.value = ""
        state.txtHarvestDate.value = ""
        state.txtWeight.value = ""
        keyModelList.value = mutableListOf()
    }

    //Bindingadapter
    fun getClear(){
        clear()
    }


    data class State (
        val txtName : MutableLiveData<String>,
        val txtGtin : MutableLiveData<String>,
        val txtSerialNumber : MutableLiveData<String>,
        val txtBatchLotNumber : MutableLiveData<String>,
        val txtPackageDate : MutableLiveData<String>,
        val txtDeliveryDate : MutableLiveData<String>,
        val txtExpiredDate : MutableLiveData<String>,
        val txtHarvestDate : MutableLiveData<String>,
        val txtWeight : MutableLiveData<String>



    )
}