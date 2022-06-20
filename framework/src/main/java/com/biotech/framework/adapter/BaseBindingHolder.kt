package com.biotech.framework.adapter



import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.biotech.framework.BR

class BaseBindingHolder(var viewDataBinding: ViewDataBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {

    fun bind(T : Any){
        viewDataBinding.setVariable(BR.key,T)
        viewDataBinding.executePendingBindings()
    }


}