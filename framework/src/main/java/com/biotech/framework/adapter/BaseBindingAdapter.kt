package com.biotech.framework.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseBindingAdapter<T>(val dataList: MutableList<T>,val layout : Int) : RecyclerView.Adapter<BaseBindingHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater,layout, parent, false)
        return BaseBindingHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: BaseBindingHolder, position: Int) {
        convertBinding(holder,position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    abstract fun convertBinding(holder: BaseBindingHolder, position: Int)

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