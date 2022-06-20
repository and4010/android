package com.biotech.framework.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


class GeneralAdapter<T1, T2 : ViewDataBinding?>(val context: Context, @LayoutRes val layout: Int, var list : MutableList<T1> = mutableListOf())
    : RecyclerView.Adapter<GeneralAdapter.BaseViewHolder>() {

    interface BindView<T2> {
        fun onBindViewHolder(b: T2, position: Int)
    }

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ViewDataBinding? = DataBindingUtil.bind(itemView)
    }

    //private var list: List<T1> = ArrayList()
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var bindView: BindView<T2>

    fun initList(list: MutableList<T1>) {
        this.list = list
    }

    fun setOnBindViewHolder(bindView: BindView<T2>) {
        this.bindView = bindView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view: View = inflater.inflate(layout, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindView.onBindViewHolder(holder.binding as T2, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addAll(data : MutableList<T1>){
        list.addAll(data)
        notifyDataSetChanged()
    }

    fun add(data : T1){
        list.add(data)
        notifyDataSetChanged()
    }

    fun removeAll(){
        list.clear()
        notifyDataSetChanged()
    }

    fun remove(data : T1){
        list.remove(data)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyDataSetChanged()
    }
}