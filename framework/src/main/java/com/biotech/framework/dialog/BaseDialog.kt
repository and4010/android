package com.biotech.framework.dialog

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.biotech.framework.R


abstract class BaseDialog : DialogFragment() {

    val TAG by lazy { this::class.java.simpleName }

    abstract val layoutRes: Int

    abstract fun initView()

    abstract fun initEvent()

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.window?.setBackgroundDrawable(null)
            it.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        it.window?.addFlags(View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_Dialog_FullScreen)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        initView()
//        initEvent()
//    }

    fun <T : View> bindView(@IdRes res: Int): Lazy<T> {
        return lazy {
            return@lazy this.requireView().findViewById<T>(res)
        }
    }

    lateinit var ctx: Context

    fun setContext(ctx: Context): BaseDialog {
        this.ctx = ctx
        return this
    }

    @Throws(NoSetContext::class)
    open fun show() {
        if (!::ctx.isInitialized)
            throw NoSetContext()

        show((ctx as AppCompatActivity).supportFragmentManager, TAG)
    }

    @Throws(NoSetContext::class)
    open fun showAllowingStateLoss() {
        if (!::ctx.isInitialized)
            throw NoSetContext()

        val ft = (ctx as AppCompatActivity).supportFragmentManager.beginTransaction()
        ft.add(this, TAG)
        ft.commitAllowingStateLoss()
    }

    class NoSetContext : Exception("no set context, need to call setContext(ctx:Context) to set context")

}