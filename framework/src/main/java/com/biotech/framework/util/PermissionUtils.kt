package com.biotech.framework.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class PermissionUtils(val context: Context) {


    fun isPermissionGranted(permission: String) : Boolean{
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }


}