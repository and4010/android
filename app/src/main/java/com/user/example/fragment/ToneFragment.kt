package com.user.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.biotech.framework.template.BaseFragment
import com.biotech.framework.util.ToneUtils
import com.user.example.R

class ToneFragment : BaseFragment(R.layout.fragment_tone) {

    val btnToneNormal by bindView<Button>(R.id.btnToneNormal)
    val btnToneWarning by bindView<Button>(R.id.btnToneWarning)
    val btnToneError by bindView<Button>(R.id.btnToneError)

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        fun getNewInstance(title : String): ToneFragment {
            return ToneFragment().apply { this.title = title }
        }
    }

    override fun initView() {
        btnToneNormal
        btnToneWarning
        btnToneError
    }

    override fun initEvent() {

    }

    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                btnToneNormal.id -> {
                    ToneUtils.playNormal()
                }
                btnToneWarning.id -> {
                    ToneUtils.playWarning()
                }
                btnToneError.id -> {
                    ToneUtils.playError()
                }
            }
        }
    }
}