package com.user.example.fragment

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.biotech.framework.dialog.EditTextDialog
import com.biotech.framework.extension.Preference
import com.biotech.framework.interfaces.DialogEditTextListener
import com.biotech.framework.template.BaseFragment
import com.biotech.framework.util.MessageUtils
import com.biotech.framework.util.ToastUtils
import com.biotech.framework.util.ToneUtils
import com.user.example.R

class TemplateButtonFragment : BaseFragment(R.layout.fragment_template_button){
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    companion object {
        @JvmStatic
        fun getNewInstance(title : String): TemplateButtonFragment {
            return TemplateButtonFragment().apply { this.title = title }
        }
    }


    val btnSuccess by bindView<Button>(R.id.btnSuccess,true)
    val btnDanger by bindView<Button>(R.id.btnDanger)
    val btnWarning by bindView<Button>(R.id.btnWarning)

    val button by bindView<Button>(R.id.button,true)
    val button2 by bindView<Button>(R.id.button2,true)

    override fun initView() {
        button
        button2
        btnSuccess
        btnDanger
        btnWarning
    }

    override fun initEvent() {

    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id){
                button.id ->{
                    var name1 by Preference(requireContext(), "name","")
                    Log.i(TAG,name1)
                        EditTextDialog(requireContext(),R.layout.dialog_edittext).show("請輸入文字",false,object :
                            DialogEditTextListener {
                            override val showPositiveButton: Boolean
                                get() = true
                            override val positiveTitle: String?
                                get() = "確定"

                            override fun positiveClick(dialog: AlertDialog,view: View) {
                                val a = view.findViewById<EditText>(R.id.editText2)
                                val name = a.text.toString()
                                ToastUtils.showToast(context!!,"你輸入: $name")
                                name1 = name
                            }
                            override val showNegativeButton: Boolean
                                get() = true
                            override val negativeTitle: String?
                                get() = "取消"
                            override fun negativeClick(dialog: AlertDialog,view: View) {

                            }
                        })
                }
                button2.id ->{
                    EditTextDialog(requireContext(),R.layout.dialog_edittext2).show("",false,object : DialogEditTextListener{
                        override val showPositiveButton: Boolean
                            get() = true
                        override val positiveTitle: String?
                            get() = "確定"

                        override fun positiveClick(dialog: AlertDialog, view: View) {
                            val a = view.findViewById<EditText>(R.id.username)
                            val b = view.findViewById<EditText>(R.id.password)
                            ToastUtils.showToast(context!!,"user:${a.text} + pass:${b.text}")
                        }

                        override val showNegativeButton: Boolean
                            get() = true
                        override val negativeTitle: String?
                            get() = "取消"

                        override fun negativeClick(dialog: AlertDialog, view: View) {

                        }

                    })
                }
                btnSuccess.id ->{
                    ToneUtils.playNormal()
                }
                btnDanger.id ->{
                    ToneUtils.playError()
                }
                btnWarning.id ->{
                    ToneUtils.playWarning()
                }
                else -> {

                }
            }
        }
    }

}

