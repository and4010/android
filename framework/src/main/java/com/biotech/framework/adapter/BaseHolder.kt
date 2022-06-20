package com.biotech.framework.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val viewArray: SparseArray<View> = SparseArray()


    /**
     * 建構ViewHolder
     *
     * @param parent 父類
     * @param resId  layoutID
     */
    constructor(parent: ViewGroup , @LayoutRes resId: Int):this(parent){
        LayoutInflater.from(parent.context).inflate(resId, parent, false)
    }



    /**
     * 獲取layout的View
     *
     * @param viewId view的id
     * @param <T>    View的類型
     * @return view
     */
    fun <T : View?> getView(@IdRes viewId: Int): T? {
        var view = viewArray[viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            viewArray.put(viewId, view)
        }
        return view as T
    }


    /**
     * 獲取Context实例
     *
     * @return context
     */
    fun getContext(): Context? {
        return itemView.context
    }




}