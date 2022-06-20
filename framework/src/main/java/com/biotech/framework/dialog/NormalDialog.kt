package com.biotech.framework.dialog

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.biotech.framework.R
import com.biotech.framework.interfaces.NormalDialogListener

class NormalDialog : BaseDialog() {

    var onCloseClickListener: View.OnClickListener? = null
    val textMessage by bindView<TextView>(R.id.text_message)
    val textTitle by  bindView<TextView>(R.id.text_title)
    val buttonClose by bindView<ImageButton>(R.id.dialog_button_close)
    val buttonConfirm by bindView<Button>(R.id.dialog_button_confirm)
    var message: String? = null
    var title : String? = null
    override val layoutRes: Int = R.layout.dialog_information
    var normalDialogListener: NormalDialogListener? = null

    companion object{
        fun getInstance(context: Context) : NormalDialog{
            val dialog = NormalDialog()
            dialog.setContext(context)
            return dialog
        }
    }


    init {
        isCancelable = false
    }



    override fun initView() {
        if (message != null) {
            textMessage.text = message
        }

        if (title != null) {
            textTitle.text = title
        }
    }

    override fun initEvent() {
        val dialog = dialog
        if(dialog != null){
            try {
                buttonClose.setOnClickListener { view: View? ->
                    if (onCloseClickListener != null) {
                        onCloseClickListener!!.onClick(view)
                    }
                    dialog.cancel()
                }

                buttonConfirm.setOnClickListener { view: View? ->
                    if (onCloseClickListener != null) {
                        onCloseClickListener!!.onClick(view)
                    }
                    dialog.cancel()
                }
            }catch (e: java.lang.Exception){
                Log.e("loadingDialog", e.message!!)
            }
        }
    }

    fun setTitle(title: String?) : NormalDialog {
        this.title = title
        return this
    }


    fun setMessage(message: String?): NormalDialog {
        this.message = message
        return this
    }

    fun setOnCloseClickListener(onCloseClickListener: View.OnClickListener?) : NormalDialog {
        this.onCloseClickListener = onCloseClickListener
        return this
    }

    fun setNormalDialogListener(normalDialogListener: NormalDialogListener?) : NormalDialog {
        this.normalDialogListener = normalDialogListener
        return this
    }

    override fun onStart() {
        super.onStart()
    }

    override fun show() {
        super.showAllowingStateLoss()


    }

    override fun dismiss() {
        try{
            super.showAllowingStateLoss()
        }catch (e: Exception){

        }

    }

    override fun onPause() {
        super.onPause()
        normalDialogListener?.onPause()
    }

    override fun onResume() {
        super.onResume()
        normalDialogListener?.onResume()
    }

}