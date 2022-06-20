package com.biotech.framework.adapter

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.biotech.framework.R
import com.biotech.framework.databinding.RecycleviewItemKeyvalueSinglelineBinding
import com.biotech.framework.model.KeyValueModel
import kotlinx.android.synthetic.main.recycleview_item_keyvalue.view.*

class RecycleViewKeyValueAdapter(val list: MutableList<KeyValueModel>) : BaseBindingAdapter<KeyValueModel>(list,
    R.layout.recycleview_item_keyvalue_singleline) {
    var row_index : Int = -1

    override fun getItemCount(): Int {
        return list.size
    }

    fun getSelectedPosition(): Int{
        return row_index
    }

    override fun convertBinding(holder: BaseBindingHolder, position: Int) {
        if (itemCount > 0 && itemCount > position && position >= 0) {
            val item = list[position]
            holder.bind(item)
        }
        holder.itemView.setOnClickListener {
            row_index = position
            notifyDataSetChanged()
        }

        if(row_index==position){
            holder.viewDataBinding.root.clytCard.setBackgroundColor(Color.parseColor("#567845"))
        }
        else
        {
            holder.viewDataBinding.root.clytCard.setBackgroundColor(Color.parseColor("#ffffff"))
        }
    }

}