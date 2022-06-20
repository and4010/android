package com.biotech.framework.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.biotech.framework.model.KeyValueModel

/**
 * Created by KNightING on 2018/10/10.
 */

class SpinnerKeyValueAdapter(
        private val ctx: Context,
        private val hint: String)
    : ArrayAdapter<KeyValueModel>(
        ctx,
        android.R.layout.simple_spinner_dropdown_item,
        mutableListOf(KeyValueModel("", hint))) {

    fun updateData(data: List<KeyValueModel>) {
        this.clear()
        add(KeyValueModel("", hint))
        data.forEach {
            add(it)
        }
        notifyDataSetChanged()
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        if(position in 0 until count) {

            if (position == 0) {
                textView.text = getItem(position)?.run { return@run value } ?: ""
                textView.setTextColor(Color.parseColor("#808080"))
            }
            else {
                textView.text = getItem(position)?.run { return@run "${key}-${value}" } ?: ""
            }
        }
        return view
    }

    override fun getCount() = super.getCount()


}