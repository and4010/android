package com.biotech.framework.extension

import android.content.Context
import android.content.Intent
import android.os.Looper
import android.os.Process
import androidx.appcompat.app.AlertDialog
import com.biotech.framework.interfaces.DialogButtonListener
import com.biotech.framework.util.DialogUtil

class ExceptionHandler(applicationContext: Context) {

    //例外捕捉錯誤
    private val restartHandler =
        Thread.UncaughtExceptionHandler { thread, ex ->
            Thread {
                Looper.prepare()
                val dialog = DialogUtil(applicationContext)
                dialog.show(
                    "錯誤",
                    ex.message!!,
                    false,
                    object : DialogButtonListener {
                        override val showPositiveButton: Boolean
                            get() = true
                        override val positiveTitle: String?
                            get() = "是"

                        override fun positiveClick(dialog: AlertDialog) {
                            val packageName = applicationContext.packageName
                            startNewActivity(applicationContext, packageName)
                            Process.killProcess(Process.myPid()) //結束程序之前可以把你程式的登出或者退出程式碼放在這段程式碼之前
                        }

                        override val showNegativeButton: Boolean
                            get() = false
                        override val negativeTitle: String?
                            get() = null

                        override fun negativeClick(dialog: AlertDialog) {

                        }

                        override val showNeutralButton: Boolean
                            get() = false
                        override val neutralTitle: String?
                            get() = null

                        override fun neutralClick(dialog: AlertDialog) {

                        }

                    }
                )
                Looper.loop()
            }.start()
        }


    fun startUncaughtException(){
        Thread.setDefaultUncaughtExceptionHandler(restartHandler)
    }

    fun startNewActivity(context: Context, packageName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }



}