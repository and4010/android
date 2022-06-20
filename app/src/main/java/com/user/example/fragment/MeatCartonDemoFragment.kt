package com.user.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biotech.framework.adapter.RecycleViewKeyValueAdapter
import com.biotech.framework.barcode.GS128
import com.biotech.framework.interfaces.DialogButtonListener
import com.biotech.framework.template.BaseFragment
import com.biotech.framework.util.DialogUtil
import com.user.example.R
import com.user.example.StartApplication
import com.user.example.carton.MeatCarton
import com.user.example.database.entity.BarcodeRule
import com.user.example.databinding.FragmentMeatCartonDemoBinding
import com.user.example.model.MeatCartonDemoViewModel

class MeatCartonDemoFragment : BaseFragment(){

    companion object {
        @JvmStatic
        fun getNewInstance(title : String): MeatCartonDemoFragment {
            return MeatCartonDemoFragment().apply { this.title = title }
        }
    }
    lateinit var meatCartonDemoViewModel: MeatCartonDemoViewModel
    lateinit var fragmentmeatcartondemo : FragmentMeatCartonDemoBinding
    val recycleViewKeyValueAdapter : RecycleViewKeyValueAdapter by lazy { RecycleViewKeyValueAdapter(mutableListOf()) }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentmeatcartondemo = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_meat_carton_demo, container, false
        )
        return fragmentmeatcartondemo.root
    }

    override fun initView() {
        meatCartonDemoViewModel = ViewModelProviders.of(this).get(MeatCartonDemoViewModel::class.java)
        fragmentmeatcartondemo.viewModel = meatCartonDemoViewModel
        initViewModel(meatCartonDemoViewModel)
        val layoutManage = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        fragmentmeatcartondemo.listAI .layoutManager = layoutManage
                fragmentmeatcartondemo.listAI.run {
            adapter = recycleViewKeyValueAdapter
        }

        meatCartonDemoViewModel.state.txtName.observe(this, Observer { fragmentmeatcartondemo.txtName.setText(it) })
        meatCartonDemoViewModel.state.txtBatchLotNumber.observe(this, Observer { fragmentmeatcartondemo.txtBatchLotNumber.setText(it) })
        meatCartonDemoViewModel.state.txtDeliveryDate.observe(this, Observer { fragmentmeatcartondemo.txtDeliveryDate.setText(it) })
        meatCartonDemoViewModel.state.txtExpiredDate.observe(this, Observer { fragmentmeatcartondemo.txtExpiredDate.setText(it) })
        meatCartonDemoViewModel.state.txtGtin.observe(this, Observer { fragmentmeatcartondemo.txtGtin.setText(it) })
        meatCartonDemoViewModel.state.txtHarvestDate.observe(this, Observer { fragmentmeatcartondemo.txtHarvestDate.setText(it) })
        meatCartonDemoViewModel.state.txtPackageDate.observe(this, Observer { fragmentmeatcartondemo.txtPackageDate.setText(it) })
        meatCartonDemoViewModel.state.txtSerialNumber.observe(this, Observer { fragmentmeatcartondemo.txtSerialNumber.setText(it) })
        meatCartonDemoViewModel.state.txtWeight.observe(this, Observer { fragmentmeatcartondemo.txtWeight.setText(it) })
        meatCartonDemoViewModel.selectBarcodeRule.observe(this, Observer { t ->
            t?.let {
                when(it){
                    true ->{
                        val barcodeRuleFragment = BarcodeRuleFragment.getNewInstance("選擇條碼規則")
                        val args = Bundle()
                        args.putParcelableArray("BarcodeRules", meatCartonDemoViewModel.getbarcodeRuleDb())
                        barcodeRuleFragment.arguments = args
                        barcodeRuleFragment.setOnResultListener {barcodeRule: BarcodeRule? ->
                            meatCartonDemoViewModel.setbarcodeRule(barcodeRule)
                        }
                        baseActivity?.showFragment(barcodeRuleFragment, R.id.content, true, false)
                    }
                    else ->{

                    }
                }
            }
        })
        meatCartonDemoViewModel.dialogRule.observe(this, Observer { t ->
            t?.let{
            when(it){
                true ->{
                    DialogUtil(requireContext()).show("固定自訂條碼規則", "是否固定此自訂條碼規則?", true, object : DialogButtonListener {
                        override val showPositiveButton: Boolean = true
                        override val positiveTitle: String? = "是"
                        override fun positiveClick(dialog: androidx.appcompat.app.AlertDialog) {
                            meatCartonDemoViewModel.setrule()
                        }


                        override val showNegativeButton: Boolean = true
                        override val negativeTitle: String? = "否"
                        override fun negativeClick(dialog: androidx.appcompat.app.AlertDialog) {
                        }

                        override val showNeutralButton: Boolean = false
                        override val neutralTitle: String? = null
                        override fun neutralClick(dialog: androidx.appcompat.app.AlertDialog) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                }
                else ->{

                }
            }
        } })


        meatCartonDemoViewModel.keyModelList.observe(this, Observer {
            recycleViewKeyValueAdapter.clear()
            if(it.size > 0) {
                recycleViewKeyValueAdapter.addAll(it)
            }
        })

        //測試資料
        if (StartApplication.instance.db.BarcodeRulesDao().getAll().count() == 0){
            StartApplication.instance.db.BarcodeRulesDao().insert(getTestBarcodeRules())
        }
    }

    override fun initEvent() {
    }



    private fun getTestBarcodeRules() : MutableList<BarcodeRule>{
        val barcodeRuleList :MutableList<BarcodeRule> = mutableListOf()
        barcodeRuleList.add(
            BarcodeRule( Name = "M-羊排", FullLength = 48, ItemNo = "99311791006367", ItemNoStartIndex = 3,
                ItemNoLength = 14, ProductionDateStartIndex = 29,
                ProductionDateLength = 6, ProductionDateFormat = "yyMMdd",
                WeightStartIndex = 21, WeightLength = 6, WeightDecimalPoint = 2,
                WeightUnit = GS128.WeightUnit.KG.ordinal, GS1 = false)
        )
        barcodeRuleList.add(
            BarcodeRule( Name = "M-羊排2", FullLength = 48, ItemNo = "99311791006367", ItemNoStartIndex = 3,
                ItemNoLength = 14, ProductionDateStartIndex = 29,
                ProductionDateLength = 6, ProductionDateFormat = "yyMMdd",
                WeightStartIndex = 21, WeightLength = 6, WeightDecimalPoint = 2,
                WeightUnit = GS128.WeightUnit.KG.ordinal, GS1 = false)
        )
        barcodeRuleList.add(
            BarcodeRule( Name = "Beef Round Heel Muscle(4pcs) (n/roll)", FullLength = 46, ItemNo = "90027182007538", ItemNoStartIndex = 3,
                ItemNoLength = 14, ProductionDateStartIndex = 29,
                ProductionDateLength = 6, ProductionDateFormat = "yyMMdd",
                WeightStartIndex = 21, WeightLength = 6, WeightDecimalPoint = 1,
                WeightUnit = GS128.WeightUnit.LB.ordinal, GS1 = false)
        )
        barcodeRuleList.add(
            BarcodeRule( Name = "XL(235A) (AA)S/P", FullLength = 44, ItemNo = "90774577041003", ItemNoStartIndex = 3,
                ItemNoLength = 14, ProductionDateStartIndex = 19,
                ProductionDateLength = 6, ProductionDateFormat = "yyMMdd",
                WeightStartIndex = 29, WeightLength = 6, WeightDecimalPoint = 2,
                WeightUnit = GS128.WeightUnit.KG.ordinal, GS1 = true)
        )
        barcodeRuleList.add(
            BarcodeRule( Name = "(1.3/2)kg Cube Roll", FullLength = 48, ItemNo = "99418220018134", ItemNoStartIndex = 3,
                ItemNoLength = 14, ProductionDateStartIndex = 29,
                ProductionDateLength = 6, ProductionDateFormat = "yyMMdd",
                WeightStartIndex = 21, WeightLength = 6, WeightDecimalPoint = 2,
                WeightUnit = GS128.WeightUnit.KG.ordinal, GS1 = false)
        )
        return barcodeRuleList
    }



    override fun onClick(p0: View?) {
    }



}

