package com.biotech.framework.interfaces

import androidx.appcompat.app.AlertDialog

interface DialogButtonListener {
    val showPositiveButton: Boolean

    val positiveTitle: String?

    fun positiveClick(dialog: AlertDialog)

    val showNegativeButton: Boolean

    val negativeTitle: String?

    fun negativeClick(dialog: AlertDialog)

    val showNeutralButton : Boolean

    val neutralTitle: String?

    fun neutralClick(dialog: AlertDialog)
}