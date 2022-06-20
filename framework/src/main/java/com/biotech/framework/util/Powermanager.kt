package com.biotech.framework.util

import android.app.Activity
import android.content.Context
import android.os.PowerManager
import android.view.WindowManager


class Powermanager(context: Context, activity: Activity) {

    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK , this::class.java.canonicalName)
    private var window = activity.window

    fun openPower(){
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) //設定螢幕不進入休眠
        wl.acquire(10*60*1000L /*10 minutes*/)
        wl.setReferenceCounted(true)
    }

    fun closePower(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) //設定螢幕不進入休眠
        if (wl.isHeld){
            wl.release()
        }
    }

}