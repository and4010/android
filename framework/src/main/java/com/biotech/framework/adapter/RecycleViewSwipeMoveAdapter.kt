package com.biotech.framework.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.biotech.framework.R
import com.biotech.framework.util.TouchHelperCallback
import com.biotech.framework.model.KeyValueModel
import java.util.*

class RecycleViewSwipeMoveAdapter(val list: MutableList<KeyValueModel>, val layoutId : Int) : RecyclerView.Adapter<RecycleViewSwipeMoveAdapter.ViewHolder>(),
    TouchHelperCallback.ItemTouchHelperCallback {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (itemCount > 0 && itemCount > position && position >= 0) {
            list[position].let {
                holder.primary.text = it.key
                holder.secondary.text = it.value
            }
        }
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val primary : TextView = view.findViewById(R.id.txtPrimary)
        val secondary : TextView = view.findViewById(R.id.txtSecondary)
        val icon : ImageView = view.findViewById(R.id.ivIcon)
    }

    override fun onItemDelete(Position: Int) {
        list.remove(KeyValueModel(list.removeAt(Position).key,list.removeAt(Position).key))
        notifyItemRemoved(Position)
//        notifyDataSetChanged()
    }

    override fun onItemMove(FromPosition: Int, ToPosition: Int) {
        Collections.swap(list,FromPosition,ToPosition)//交换数据
        notifyItemMoved(FromPosition,ToPosition)
    }

    fun clear() {
        this.list.clear()
        this.notifyDataSetChanged()
    }

    fun remove(model: KeyValueModel){
        this.list.remove(model)
        this.notifyDataSetChanged()
    }

    fun addAll(mutableList: MutableList<KeyValueModel>) {
        this.list.addAll(mutableList)
        this.notifyDataSetChanged()
    }

    fun add(Model: KeyValueModel){
        this.list.add(Model)
        this.notifyItemInserted(this.list.size - 1)
    }


}