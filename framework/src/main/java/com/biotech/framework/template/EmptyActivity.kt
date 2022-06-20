package com.biotech.framework.template


import androidx.annotation.LayoutRes
import com.biotech.framework.template.BaseActivity
import com.biotech.framework.template.BaseFragment


abstract class EmptyActivity(@LayoutRes private val layoutRes: Int,val fragment: BaseFragment): BaseActivity(layoutRes) {

}

