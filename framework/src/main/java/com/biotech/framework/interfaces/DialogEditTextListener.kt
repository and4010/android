package com.biotech.framework.interfaces

import androidx.appcompat.app.AlertDialog
import android.view.View

interface DialogEditTextListener {
    val showPositiveButton: Boolean

    val positiveTitle: String?

    fun positiveClick(dialog: AlertDialog,view: View)

    val showNegativeButton: Boolean

    val negativeTitle: String?

    fun negativeClick(dialog: AlertDialog,view: View)
}