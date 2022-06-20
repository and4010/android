package com.user.example

import android.view.View
import com.biotech.framework.template.EmptyActivity
import com.user.example.fragment.MeatCartonDemoFragment
import android.app.Activity
import androidx.fragment.app.Fragment
import com.biotech.framework.interfaces.IOnBackPressed
import com.biotech.framework.template.BaseFragment
import com.biotech.framework.util.FragmentUtil
import kotlinx.android.synthetic.main.nav_header_main.*


class MeatCartonDemoActivity: EmptyActivity(R.layout.activity_empty, MeatCartonDemoFragment.getNewInstance("GS1 肉品條碼展示")) {

    override fun initView() {
        showFragment(fragment, R.id.content)
    }

    override fun initEvent() {
    }

    override fun onClick(p0: View?) {

    }

    override fun onBackPressed() {
      val currentFragment = FragmentUtil().getCurrentFragment(this)
        if (currentFragment != null && currentFragment is IOnBackPressed ){
            if ((currentFragment as IOnBackPressed).onBackPressed()) {
                // If the onBackPressed override in your fragment
                // did absorb the back event (returned true), return
                return
            } else {
                // Otherwise, call the super method for the default behavior
                if (supportFragmentManager.backStackEntryCount >= 2) {   //要有兩個以上的backStack才能取得上一個Fragment的title
                    title = (FragmentUtil().getPreviousFragment(this) as BaseFragment).title
                }
                super.onBackPressed()
            }
        }else{
            super.onBackPressed()
        }
    }
}
