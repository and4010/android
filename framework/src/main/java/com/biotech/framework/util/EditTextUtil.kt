package com.biotech.framework.util

import android.content.Context
import android.util.Log
import android.widget.EditText
import java.lang.reflect.Method

class EditTextUtil {

    companion object{
        fun initKeyboard(editText: EditText, context: Context, softInput: Boolean) {

            if (softInput) {
                enableSoftInput(editText)
            } else {
                disableSoftInput(editText)
            }

        }


        private fun enableSoftInput(editText: EditText){
            val cls = EditText::class.java
            val method : Method
            try{
                method = cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaObjectType)
                method.isAccessible = false
                method.invoke(editText,true)
            }catch (e: Exception){
                Log.e("EditKeyPad", e.message!!)
            }
        }

        private fun disableSoftInput(editText: EditText){
            val cls = EditText::class.java
            val method : Method
            try{
                method = cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaObjectType)
                method.isAccessible = true
                method.invoke(editText,false)
            }catch (e: Exception){
                Log.e("EditKeyPad", e.message!!)
            }
        }

    }


}