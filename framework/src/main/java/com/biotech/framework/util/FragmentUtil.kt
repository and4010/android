package com.biotech.framework.util

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class FragmentUtil{

    fun getVisibleFragment(activity : FragmentActivity): Fragment? {
        val fragmentManager = activity.supportFragmentManager
        val fragments = fragmentManager.fragments
        for (fragment in fragments) {
            if (fragment != null && fragment.isVisible)
                return fragment
        }
        return null
    }

    fun getCurrentFragment(activity : FragmentActivity): Fragment? {
        val fragmentManager = activity.supportFragmentManager
        if (fragmentManager.backStackEntryCount >= 1) {
            val lastFragmentName = fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1).name
            return fragmentManager.findFragmentByTag(lastFragmentName)
        }
        return null
    }

    fun getPreviousFragment(activity : FragmentActivity): Fragment? {
        val fragmentManager = activity.supportFragmentManager
        if (fragmentManager.backStackEntryCount >= 2) {
            val lastFragmentName = fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 2).name
            return fragmentManager.findFragmentByTag(lastFragmentName)
        }
        return null
    }

}