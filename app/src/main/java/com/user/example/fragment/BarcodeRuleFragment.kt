package com.user.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biotech.framework.interfaces.IOnBackPressed
import com.user.example.model.BarcodeRuleViewModel
import com.biotech.framework.template.BaseFragment
import com.biotech.framework.util.ToastUtils
import com.user.example.R
import com.user.example.database.entity.BarcodeRule
import com.user.example.databinding.FragmentBarcoderuleBinding


class BarcodeRuleFragment : BaseFragment() , IOnBackPressed {

    companion object {
        @JvmStatic
        fun getNewInstance(title: String): BarcodeRuleFragment {
            return BarcodeRuleFragment().apply { this.title = title }
        }
    }

    private var mListener : ((BarcodeRule?) -> Unit)? = null

    private var barcodeRuleArray : Array<BarcodeRule>? = null

    fun setOnResultListener(mListener : (barcodeRule: BarcodeRule?) -> Unit){
        this.mListener = mListener
    }

    lateinit var barcodeRuleViewModel : BarcodeRuleViewModel
    lateinit var fragmentbarcoderule : FragmentBarcoderuleBinding

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentbarcoderule = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_barcoderule, container, false
        )
        return fragmentbarcoderule.root
    }

    override fun initView() {
        barcodeRuleViewModel = ViewModelProviders.of(this).get(BarcodeRuleViewModel::class.java)
        fragmentbarcoderule.viewModel = barcodeRuleViewModel
        val layoutManage = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        fragmentbarcoderule.barcodeRuleRecyclerView.layoutManager = layoutManage
        fragmentbarcoderule.barcodeRuleRecyclerView.run {
            adapter = barcodeRuleViewModel.recycleViewKeyValueAdapter
        }

        barcodeRuleArray = arguments?.getParcelableArray("BarcodeRules") as Array<BarcodeRule>
        barcodeRuleViewModel.barcodeModel.barcodeRuleArray = barcodeRuleArray
        barcodeRuleViewModel.setInsertdata()
        barcodeRuleViewModel.mListener = mListener

        barcodeRuleViewModel.confirm.observe(this, Observer { t ->
            t.let{
                when(it){
                    false ->{
                        ToastUtils.showToast(this.requireContext(),"請選擇資料",R.raw.error,true)
                    }
                    true ->{
                        requireActivity().onBackPressed()
                    }
                }
            }

        })
        barcodeRuleViewModel.cancel.observe(this, Observer { t ->
            t.let{
                when(it){
                    true ->{
                        requireActivity().onBackPressed()
                    }
                }
            }

        })
    }

    override fun initEvent() {

    }


    override fun onClick(v: View?) {
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}