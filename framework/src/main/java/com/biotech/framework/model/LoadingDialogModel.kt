package com.biotech.framework.model

import android.content.DialogInterface

class LoadingDialogModel(
    var message: String = "",
    var isCancelable: Boolean = false,
    var isShowing: Boolean = false,
    var onCancelListener : DialogInterface.OnCancelListener? = null )
{

}

