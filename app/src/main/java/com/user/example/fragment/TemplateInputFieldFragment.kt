package com.user.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import com.biotech.framework.adapter.SpinnerHintAdapter
import com.biotech.framework.template.BaseFragment
import com.user.example.R


class TemplateInputFieldFragment : BaseFragment(R.layout.fragment_template_inputfield){
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        @JvmStatic
        fun getNewInstance(title : String): TemplateInputFieldFragment {
            return TemplateInputFieldFragment().apply { this.title = title }
        }
    }


    val spinnertest by bindView<Spinner>(R.id.spinner)
    val spinnertest2 by bindView<Spinner>(R.id.spinner2)
    private val spinneradapter by lazy { SpinnerHintAdapter(requireContext(), "HELLLO") }
    private val spinneradapter2 by lazy { SpinnerHintAdapter(requireContext(), "HELLLO2") }


    override fun initView() {

        spinnertest.run {
            adapter = spinneradapter.apply {
                updateData((1..50).toList().map { it.toString() })
            }

        }

        spinnertest2.run {
            adapter = spinneradapter2.apply {
                updateData((1..50).toList().map { it.toString() })
            }

        }
    }

    override fun initEvent() {

    }

    override fun onClick(p0: View?) {

    }

}

