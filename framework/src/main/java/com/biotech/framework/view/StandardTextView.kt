package com.biotech.framework.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import android.widget.LinearLayout
import com.biotech.framework.R
import kotlinx.android.synthetic.main.view_standard_edittext.view.*
import kotlinx.android.synthetic.main.view_standard_textview.view.*
import kotlinx.android.synthetic.main.view_standard_textview.view.txtKey
import java.util.concurrent.atomic.AtomicInteger


class StandardTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

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
            .inflate(R.layout.view_standard_textview, this, true)

        context.obtainStyledAttributes(attrs,R.styleable.StandardTextView, defStyle, 0).apply {
            val text = getString(R.styleable.StandardTextView_android_text)
            val title = getString(R.styleable.StandardTextView_android_title)
            val textSize = getDimension(R.styleable.StandardTextView_android_textSize, 10f)
            val icon = getDrawable(R.styleable.StandardTextView_android_icon)

            txtValue.id = generateViewId()

            if (textSize > 0f) {
                setTextSize(textSize)
            }

            if (text != null && text.isNotEmpty()) {
                setText(text)
            }

            if (title != null && title.isNotEmpty()) {
                setTitle(title)
            }

            if (icon != null) {
                setImage(icon)
            }

            recycle()
        }
    }

    fun setTitle(title : String?) {
        txtKey.text = title
    }

    fun setText(text : String) {
        txtValue.text = text
    }

    fun setTextSize(size : Float) {
        txtValue.textSize = size
        txtKey.textSize = size - if(size > 12f) 6f else 0f
    }

    fun setImage(drawable : Drawable) {
        txtKey.setCompoundDrawables(drawable, null, null, null)
    }

    fun getValueTextView() = txtKey

    fun getKeyTextView() = txtKey

}