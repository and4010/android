package com.biotech.framework.util

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText


class OnKeyHandler(var editText: EditText) : View.OnKeyListener,
    TextWatcher {
    var selectedLength = 0
    var maxLength = 0
    var enableMaxLength = true
    var isPlayKeySound = true
    var ignoreKeySound: Long = 0
    var timesOfPlaySound = 0
    var isIgnoreSoundWhenQicklyInput = true
    var onKeyHandleListener: OnKeyHandleListener? = null

    interface OnKeyHandleListener {
        fun onBackward()
        fun onStringInput(input: String?): Boolean
    }

    fun setMaxLength(maxLength: Int): OnKeyHandler {
        this.maxLength = maxLength
        return this
    }

    fun setOnKeyHandleListener(onKeyHandleListener: OnKeyHandleListener?): OnKeyHandler {
        this.onKeyHandleListener = onKeyHandleListener
        return this
    }

    fun setPlayKeySound(playKeySound: Boolean): OnKeyHandler {
        isPlayKeySound = playKeySound
        return this
    }

    fun setIgnoreSoundWhenQicklyInput(ignoreSoundWhenQicklyInput: Boolean): OnKeyHandler {
        isIgnoreSoundWhenQicklyInput = ignoreSoundWhenQicklyInput
        return this
    }

    fun enableMaxLength(enable: Boolean): OnKeyHandler {
        enableMaxLength = enable
        return this
    }

    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
        val input = (v as EditText).text.toString().trim { it <= ' ' }
        val editText = v
        if (!editText.isEnabled) {
            return false
        }
        if (event.action == KeyEvent.ACTION_DOWN) {
            if (isPlayKeySound && !isQuicklyInputed) {
                playSound(keyCode)
            }
            selectedLength = editText.selectionEnd
            return false
        }
        when (keyCode) {
            KeyEvent.KEYCODE_DEL -> {
                if (selectedLength > 0) {
                    return false
                } else if (input.length > 0) {
                    return false
                }
                if (onKeyHandleListener != null) {
                    onKeyHandleListener!!.onBackward()
                }
                return true
            }
            KeyEvent.KEYCODE_ESCAPE -> {
                if (onKeyHandleListener != null) {
                    onKeyHandleListener!!.onBackward()
                }
                return true
            }
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> return onKeyHandleListener != null && onKeyHandleListener!!.onStringInput(
                input
            )
        }
        return false
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        if (!editText.isEnabled) {
            return
        }
        if (enableMaxLength && s.length == maxLength) {
            onKeyHandleListener!!.onStringInput(s.toString().trim { it <= ' ' })
        }
    }

    private fun playSound(keyCode: Int) {
        Thread {
            when (keyCode) {
                KeyEvent.KEYCODE_0, KeyEvent.KEYCODE_NUMPAD_0, KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_NUMPAD_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_NUMPAD_2, KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_NUMPAD_3, KeyEvent.KEYCODE_4, KeyEvent.KEYCODE_NUMPAD_4, KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_NUMPAD_5, KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_NUMPAD_6, KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_NUMPAD_7, KeyEvent.KEYCODE_8, KeyEvent.KEYCODE_NUMPAD_8, KeyEvent.KEYCODE_9, KeyEvent.KEYCODE_NUMPAD_9, KeyEvent.KEYCODE_NUMPAD_DOT -> ToneUtils.playNumberKey()
                KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_B, KeyEvent.KEYCODE_C, KeyEvent.KEYCODE_D, KeyEvent.KEYCODE_E, KeyEvent.KEYCODE_F, KeyEvent.KEYCODE_G, KeyEvent.KEYCODE_H, KeyEvent.KEYCODE_I, KeyEvent.KEYCODE_J, KeyEvent.KEYCODE_K, KeyEvent.KEYCODE_L, KeyEvent.KEYCODE_M, KeyEvent.KEYCODE_N, KeyEvent.KEYCODE_O, KeyEvent.KEYCODE_P, KeyEvent.KEYCODE_Q, KeyEvent.KEYCODE_R, KeyEvent.KEYCODE_S, KeyEvent.KEYCODE_T, KeyEvent.KEYCODE_U, KeyEvent.KEYCODE_V, KeyEvent.KEYCODE_W, KeyEvent.KEYCODE_X, KeyEvent.KEYCODE_Y, KeyEvent.KEYCODE_Z, KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_DEL, KeyEvent.KEYCODE_ENTER -> ToneUtils.playAphaKey()
            }
        }.start()
    }

    private val isQuicklyInputed: Boolean
        private get() {
            val time = System.currentTimeMillis()
            if (time - ignoreKeySound < 150) {
                return true
            }
            if (time - ignoreKeySound > 500) {
                timesOfPlaySound = 0
            }
            if (timesOfPlaySound > 3) {
                return true
            }
            timesOfPlaySound++
            ignoreKeySound = time
            return false
        }

    init {
        editText.setOnKeyListener(this)
        editText.addTextChangedListener(this)
    }
}