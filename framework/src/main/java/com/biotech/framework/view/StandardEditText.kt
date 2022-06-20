package com.biotech.framework.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import com.biotech.framework.R
import kotlinx.android.synthetic.main.view_standard_edittext.view.*
import org.jetbrains.anko.image
import java.util.concurrent.atomic.AtomicInteger


class StandardEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    companion object {
        private val sNextGeneratedId: AtomicInteger = AtomicInteger(1)

        fun generateViewId(): Int {
            while (true) {
                val result: Int = sNextGeneratedId.get()
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                var newValue = result + 1
                if (newValue > 0x00FFFFFF) newValue = 1 // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result
                }
            }
        }
    }

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_standard_edittext, this, true)

        context.obtainStyledAttributes(attrs,R.styleable.StandardEditText, defStyle, 0).apply {
            val text = getString(R.styleable.StandardEditText_android_text)
            val hint = getString(R.styleable.StandardEditText_android_hint)
            val title = getString(R.styleable.StandardEditText_android_title)
            val textSize = getDimension(R.styleable.StandardEditText_android_textSize, 10f)
            val icon = getDrawable(R.styleable.StandardEditText_android_icon)
            val pattern = getInteger(R.styleable.StandardEditText_pattern, 0)
            val inputType = getInteger(R.styleable.StandardEditText_android_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL)
            val maxLength = getInteger(R.styleable.StandardEditText_android_maxLength, 255)
            val selectAllOnFocus = getBoolean(R.styleable.StandardEditText_android_selectAllOnFocus,true)

            if (textSize > 0f) {
                setTextSize(textSize)
            }

            if (text != null && text.isNotEmpty()) {
                setText(text)
            }

            if (hint != null && hint.isNotEmpty()) {
                setHint(hint)
            }

            if (title != null && title.isNotEmpty()) {
                setTitle(title)
            }

            if (icon != null) {
                setImage(icon)
            }

            if (maxLength > 0) {
                setMaxLength(maxLength)
            }

            if (selectAllOnFocus){
                setselectAllOnFocus(true)
            }

            edtValue.inputType = inputType
            edtValue.id = generateViewId()
            when (pattern) {
                1 -> {
                    btnAction.visibility = View.GONE
                }
                2 -> {
                    txtKey.visibility = View.GONE
                }
                3 -> {
                    txtKey.visibility = View.GONE
                    btnAction.visibility = View.GONE
                }
                else -> {
                }
            }


            recycle()
        }
    }

    fun setTitle(title : String?) {
        txtKey.text = title
    }

    fun setText(text : String) {
        edtValue.setText(text)
    }

    fun setHint(hint: String) {
        edtValue.hint = hint
    }

    fun setTextSize(size : Float) {
        edtValue.textSize = size
        txtKey.textSize = size - if(size > 12f) 6f else 0f
    }

    fun setImage(drawable : Drawable) {
        btnAction.image = drawable
    }

    fun setOnActionClickListener(onClickListener: OnClickListener) {
        btnAction.setOnClickListener(onClickListener)
    }

    fun getEditText() = edtValue

    fun getTextView() = txtKey

    fun getButton() = btnAction

    fun setMaxLength(maxLength : Int) {
        edtValue.filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }

    fun setselectAllOnFocus(boolean: Boolean){
        edtValue.setSelectAllOnFocus(boolean)
    }



}