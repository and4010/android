package com.user.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.biotech.framework.template.BaseFragment
import com.user.example.R
import com.user.example.viewmodel.LoadingDialogDemoViewModel

class LoadingDialogDemoFragment : BaseFragment(R.layout.fragment_loading_dialog_demo) {

    val btnCancelable by bindView<Button>(R.id.btnCancelable)
    val btnNoneCancelable by bindView<Button>(R.id.btnNoneCancelable)
    lateinit var loadingDialogDemoViewModel: LoadingDialogDemoViewModel

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        fun getNewInstance(title : String): LoadingDialogDemoFragment {
            return LoadingDialogDemoFragment().apply { this.title = title }
        }
    }

    override fun initView() {
        btnCancelable
        btnNoneCancelable
    }

    override fun initEvent() {
        loadingDialogDemoViewModel = ViewModelProvider(this).get(LoadingDialogDemoViewModel::class.java)
        initViewModel(loadingDialogDemoViewModel)
    }

    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                btnCancelable.id -> {
                    loadingDialogDemoViewModel.showLoadingDialogCancelable()
                }
                btnNoneCancelable.id -> {
                    loadingDialogDemoViewModel.showLoadingDialog()
                }

            }
        }
    }

}