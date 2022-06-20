package com.biotech.framework.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Created by KNightING on 2018/10/10.
 */

class SpinnerHintAdapter
    : ArrayAdapter<String> {

    private var hint: String
    private var data: MutableList<String>

    constructor(ctx: Context, hint: String, layoutId: Int, data: MutableList<String>) : super(
            ctx,
            layoutId,
            data) {
        this.hint = hint
        this.data = data
    }

    constructor(ctx: Context, hint: String, layoutId : Int) : this(
        ctx,
        hint,
        layoutId,
        mutableListOf(hint))

    constructor(ctx: Context, hint: String) : this(ctx, hint, android.R.layout.simple_spinner_dropdown_item,mutableListOf(hint))

    fun updateData(data: List<String>) {
        this.clear()
        this.data.add(hint)
        data.forEach {
            this.data.add(it)
        }
        notifyDataSetChanged()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        if (position == 0) {
            val tv = view.findViewById<TextView>(android.R.id.text1)
            tv.setTextColor(Color.parseColor("#808080"))
        }
        return view
    }

    private var dropViewHeight: Int = 0

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        view.run {
            if (position == 0) {
                layoutParams = view.layoutParams.apply {
                    if (dropViewHeight == 0) dropViewHeight = height
                    height = 0
                }
                visibility = View.GONE
            } else {
                layoutParams = view.layoutParams.apply { height = dropViewHeight }
                visibility = View.VISIBLE
            }
        }
        return view
    }

    override fun getCount() = data.size
}