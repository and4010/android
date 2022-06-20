package com.biotech.framework.template

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.widget.AdapterView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.biotech.framework.R
import com.biotech.framework.extension.ExceptionHandler
import com.biotech.framework.interfaces.IOnBackPressed
import com.biotech.framework.util.FragmentUtil
import java.util.*


abstract class BaseActivity(@LayoutRes private val layoutRes: Int) : AppCompatActivity(),View.OnClickListener {

    val TAG = this.javaClass.simpleName

    val viewStub by bindView<ViewStub>(R.id.viewStub, false)

    abstract fun initView()

    abstract fun initEvent()

    public override fun onCreate(savedInstanceState: Bundle?) {
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT  //螢幕方向
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        viewStub.layoutResource = layoutRes
        viewStub.inflate()
        ExceptionHandler(this).startUncaughtException()
        initView()
        initEvent()
    }

    protected fun <T : View> bindView(@IdRes id: Int, isSetOnClickListener: Boolean = true): Lazy<T> {
        return lazy {
            val view: T = findViewById(id)
            if (isSetOnClickListener && view !is AdapterView<*>)
                view.setOnClickListener(this)
            return@lazy view
        }
    }

    fun showFragment(
        fragment: BaseFragment,
        isAddToBack: Boolean = false,
        isClearBackStack: Boolean = true
    ) {
        showFragment(fragment, R.id.FrameLayout, isAddToBack, isClearBackStack)
    }


    fun showFragment(
        fragment: BaseFragment,
        layoutRes: Int,
        isAddToBack: Boolean = false,
        isClearBackStack: Boolean = true
    ) {

        val nowFragment = supportFragmentManager.findFragmentById(layoutRes) as BaseFragment?

        if (nowFragment != null)
            if (nowFragment.isAdded && nowFragment.TAG == fragment.TAG)
                return

        supportFragmentManager.commit {
            if (isClearBackStack)
                clearBackStackFragment()

            replace(layoutRes, fragment, fragment.TAG)
            if (isAddToBack)
                addToBackStack(fragment.TAG)
        }

        title = fragment.title

    }

    private fun clearBackStackFragment() {
        val fm = supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }

    override fun onBackPressed() {

        val currentFragment = FragmentUtil().getCurrentFragment(this)
        if (currentFragment != null && currentFragment is IOnBackPressed) {
            if ((currentFragment as IOnBackPressed).onBackPressed()) {
                // If the onBackPressed override in your fragment
                // did absorb the back event (returned true), return
                return
            } else {
                // Otherwise, call the super method for the default behavior
                when {
                    supportFragmentManager.backStackEntryCount > 0 -> {
                        supportFragmentManager.popBackStack()
                    }
                    else -> {
                        super.onBackPressed()
                    }
                }
            }
        } else {
            super.onBackPressed()
        }
    }
}