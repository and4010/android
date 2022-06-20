package com.biotech.framework.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.biotech.framework.interfaces.DialogButtonListener

class DialogUtil(val context: Context) {

    fun show(
        title: String,
        msg: String,
        cancellable: Boolean = false,
        listener: DialogButtonListener? = object : DialogButtonListener {
            override val showPositiveButton: Boolean = true
            override val positiveTitle: String? = null
            override fun positiveClick(dialog: AlertDialog) {}

            override val showNegativeButton: Boolean = false
            override val negativeTitle: String? = null
            override fun negativeClick(dialog: AlertDialog) {}

            override val showNeutralButton: Boolean = false
            override val neutralTitle: String? = null
            override fun neutralClick(dialog: AlertDialog) {}
        }
    ) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setCancelable(cancellable)

        listener?.let {
            if (it.showPositiveButton) {
                builder.setPositiveButton(it.positiveTitle ?: "確定") { _, _ ->
                    it.positiveClick(dialog!!)
                    dialog?.dismiss()
                }
            }
            if (it.showNegativeButton) {
                builder.setNegativeButton(it.negativeTitle ?: "取消") { _, _ ->
                    it.negativeClick(dialog!!)
                    dialog?.dismiss()
                }
            }
            if (it.showNeutralButton) {
                builder.setNeutralButton(it.neutralTitle ?: "套用") { _, _ ->
                    it.neutralClick(dialog!!)
                }
            }
        }

        if (listener == null)
            builder.setPositiveButton("確定") { _, _ ->
                dialog?.dismiss()
            }

        dialog = builder.create()
        dialog.show()

    }
}