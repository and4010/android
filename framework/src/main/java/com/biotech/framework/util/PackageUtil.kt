package com.biotech.framework.util

import android.content.Context
import android.os.Build

class PackageUtil(val context : Context) {
    fun getVersionCode():Int {
        val packinfo = context.packageManager.getPackageInfo(getPackageName(), 0)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packinfo.longVersionCode.toInt()
        } else {
            packinfo.versionCode
        }
    }

    fun getPackageName() : String = context.packageName
}