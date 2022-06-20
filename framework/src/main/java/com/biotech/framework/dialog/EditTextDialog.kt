package com.biotech.framework.dialog

import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.biotech.framework.interfaces.DialogEditTextListener


class EditTextDialog(val context: Context,var layoutRes: Int){
    fun show(
        title: String,
        cancellable: Boolean = false,
        listener: DialogEditTextListener? = object : DialogEditTextListener {

            override val showPositiveButton: Boolean = true
            override val positiveTitle: String? = null
            override fun positiveClick(dialog: AlertDialog,view: View) {}

            override val showNegativeButton: Boolean = false
            override val negativeTitle: String? = null
            override fun negativeClick(dialog:AlertDialog,view: View) {}


        }
    ) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        var item = LayoutInflater.from(context).inflate(layoutRes,null)

        builder.setTitle(title)
        builder.setView(item)
        builder.setCancelable(cancellable)
        listener?.let {
            if (it.showPositiveButton) {
                builder.setPositiveButton(it.positiveTitle ?: "確定") { _, _ ->
                    it.positiveClick(dialog!!,item)
                    dialog?.dismiss()
                }
            }
            if (it.showNegativeButton) {
                builder.setNegativeButton(it.negativeTitle ?: "取消") { _, _ ->
                    it.negativeClick(dialog!!,item)
                    dialog?.dismiss()
                }
            }
        }

        if (listener == null)
            builder.setPositiveButton("確定") { _, _ ->
                dialog?.dismiss()
            }

        dialog = builder.create()
        dialog?.show()
    }

}



