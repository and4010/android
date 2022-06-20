package com.user.example.model

import androidx.databinding.BindingAdapter
import com.biotech.framework.view.StandardEditText
import androidx.databinding.InverseBindingAdapter
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.InverseBindingListener

var syandardEditTextCallBack: ((value:String) -> Unit)? = null
var syandardButtonCallBack: ((view:View) -> Unit)? = null
@BindingAdapter("editTextValueAttrChanged")
fun setListener(standardEditText: StandardEditText, listener: InverseBindingListener) {
    standardEditText.getEditText().addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            listener.onChange()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    })
}

@BindingAdapter("editTextValue")
fun setTextValue(standardEditText: StandardEditText, value: String?) {
    if (value != standardEditText.getEditText().text.toString()) standardEditText.getEditText().setText(value)
    if (syandardEditTextCallBack != null) {
        value?.let { syandardEditTextCallBack!!.invoke(it) }
    }
}

@InverseBindingAdapter(attribute = "editTextValue")
fun getTextValue(standardEditText: StandardEditText): String? {
    return standardEditText.getEditText().text.toString()
}


@BindingAdapter("getButton")
fun getbutton(standardEditText: StandardEditText, onClickListener: View.OnClickListener) {
    if (onClickListener != standardEditText.getButton()){
        standardEditText.setOnActionClickListener(View.OnClickListener {
            if (syandardButtonCallBack != null) {
                syandardButtonCallBack!!.invoke(it)
            }
        })
    }
}
