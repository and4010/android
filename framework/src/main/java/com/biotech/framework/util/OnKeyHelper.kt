package com.biotech.framework.util

import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView

class OnKeyHelper : View.OnKeyListener, TextView.OnEditorActionListener {

    var onKeyListener: OnKeyListener? = null
    var checkInputInterface: CheckInputInterface? = null

    interface OnKeyListener {
        fun onKey(_input: String?): Boolean
    }

    constructor(_onKeyListener : OnKeyListener){
        this.onKeyListener = _onKeyListener
    }

    interface CheckInputInterface {
        fun checkInput(_input: String?, _view: View?): Boolean
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {

        val input = (v as EditText).text.toString().trim { it <= ' ' }
            if(keyCode != KeyEvent.KEYCODE_ENTER)
            {
                v.error = null
            }

        if(input.isNotEmpty() && event!!.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER){
            if(onKeyListener != null){
                this.onKeyListener!!.onKey(input)
            }
        }
        return (keyCode == KeyEvent.KEYCODE_ENTER)
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        val input = (v as TextView).text.toString().trim { it <= ' ' }

        if (input.isNotEmpty() && (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT)) {
            val viewName =
                if (v.id != -1) v.context.resources.getResourceEntryName(v.id) else "None"
            if (this.checkInputInterface != null && !this.checkInputInterface!!.checkInput(
                    input,
                    v
                )
            ) return false
            if (actionId == EditorInfo.IME_ACTION_DONE) onKeyListener!!.onKey(input)
        }

        return actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
    }


}