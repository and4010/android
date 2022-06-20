package com.biotech.framework.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.biotech.framework.R
import java.lang.Exception


class LoadingDialog
    : BaseDialog() {

    companion object {
        fun getInstance(context : Context) : LoadingDialog {
            return LoadingDialog().apply { setContext(context) }
        }
    }
    val tvMessage by bindView<TextView>(R.id.tvMessage)
    val btnCancel by bindView<Button>(R.id.btnCancel)
    var onCancelListener : DialogInterface.OnCancelListener? = null

    init {
        isCancelable = false
    }

    override val layoutRes: Int = R.layout.dlg_loading

    lateinit var message: String

    fun setMessage(message: String): LoadingDialog {
        this.message = message
        return this
    }

    fun setCancel(cancelable : Boolean) : LoadingDialog {
        isCancelable = cancelable
        return this
    }

    fun setOnCancelListener(listener : DialogInterface.OnCancelListener?) : LoadingDialog {
        onCancelListener = listener
        return this
    }

    fun changeMessage(message : String){
        this.message = message
        tvMessage.text = this.message
    }

    override fun initView() {
        if (::message.isInitialized)
            tvMessage.text = this.message

        btnCancel.visibility = if (isCancelable) View.VISIBLE else View.GONE
    }

    override fun initEvent() {
        val onCancelClick : (v : View) -> Unit = { _ ->
            dialog?.cancel()
        }
        btnCancel.setOnClickListener(onCancelClick)
    }

    override fun show() {
        super.showAllowingStateLoss()
    }

    override fun dismiss() {
        try {
            super.dismissAllowingStateLoss()
        }catch (ex : Exception) {

        }
    }

    override fun onStart() {
        super.onStart()
        if(onCancelListener != null) {
            dialog?.setOnCancelListener(onCancelListener)
        }
    }
}