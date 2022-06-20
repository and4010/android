package com.biotech.framework.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(val context: Context,val dataList: MutableList<T>,val layout : Int) : RecyclerView.Adapter<BaseHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return BaseHolder(LayoutInflater.from(context).inflate(layout, parent, false))
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        convert(holder, position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * 給Adapter
     */
    abstract fun convert(holder: BaseHolder, position: Int)

    fun addAll(data : MutableList<T>){
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun add(data : T){
        dataList.add(data)
        notifyDataSetChanged()
    }

    fun clear(){
        dataList.clear()
        notifyDataSetChanged()
    }

    fun remove(data : T){
        dataList.remove(data)
        notifyDataSetChanged()
    }
}