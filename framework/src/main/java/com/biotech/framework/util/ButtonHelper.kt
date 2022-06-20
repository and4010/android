package com.biotech.framework.util

import android.util.Log
import android.view.View

class ButtonHelper(var listener : View.OnClickListener) : View.OnClickListener {

    private var lastClickTime: Long = 0

    private val timeInterval: Long = 2000

    private val disableWhenNotVisible = true

    var buttonClickListener : ((View?) -> Unit)? = null

    init {

    }


    override fun onClick(v: View?) {

        if (isFastDoubleClick()) {
            return
        }

        if (disableWhenNotVisible && v?.visibility != View.VISIBLE) {
            return
        }

        if (buttonClickListener != null) {
            buttonClickListener!!.invoke(v)
        }


    }


    //TODO 防止點擊
    fun isFastDoubleClick(): Boolean {
        val time = System.currentTimeMillis()
        Log.e("Button", "isFastDoubleClick: " + (time - lastClickTime))
        if (time - lastClickTime < timeInterval) {
            return true
        }
        lastClickTime = time
        return false
    }
}