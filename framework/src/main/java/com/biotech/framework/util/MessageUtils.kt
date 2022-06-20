package com.biotech.framework.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.RequiresApi


class MessageUtils{

    companion object{

        var vibrator : Vibrator? = null

        @RequiresApi(Build.VERSION_CODES.M)
        fun showNormalMessage(context: Context, msg: String?, turnOnSound: Boolean = true, turnOnVibration: Boolean = true){

            if (turnOnSound){
                //鈴聲
                Thread{
                    ToneUtils.playNormal()
                }.start()
            }


            if (!msg.isNullOrEmpty()){
                ToastUtils.showToast(context, msg, Toast.LENGTH_SHORT, Gravity.CENTER)
            }

            if (turnOnVibration){
                //震動
                vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator!!.vibrate(VibrationEffect.createOneShot(100, 50))
                }
            }

        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun showWarningMessage(context: Context, msg: String?, turnOnSound: Boolean = true, turnOnVibration: Boolean = true){

            if (turnOnSound){
                //鈴聲
                Thread{
                    ToneUtils.playWarning()
                }.start()
            }

            if (!msg.isNullOrEmpty()) {
                ToastUtils.showToast(context, msg, Toast.LENGTH_SHORT, Gravity.CENTER)
            }

            if (turnOnVibration){
                //震動
                vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator!!.vibrate(VibrationEffect.createOneShot(100, 100))
                }
            }

        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun showErrorMessage(context: Context, msg: String?, turnOnSound: Boolean = true, turnOnVibration: Boolean = true){

            if (turnOnSound){
                //鈴聲
                Thread{
                    ToneUtils.playError()
                }.start()
            }


            //訊息
            if (!msg.isNullOrEmpty()) {
                ToastUtils.showToast(context, msg, Toast.LENGTH_LONG, Gravity.CENTER)
            }

            if (turnOnVibration){
                val timings = longArrayOf(0, 200, 200, 200, 200, 200)
                //震動
                vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator!!.vibrate(VibrationEffect.createWaveform(timings, -1))
                }
            }

        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun showNormalMessage(context: Context, resourceId: Int){
            showNormalMessage(context, context.resources.getString(resourceId))
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun showWarningMessage(context: Context, resourceId: Int){
            showWarningMessage(context,context.resources.getString(resourceId))
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun showErrorMessage(context: Context,resourceId: Int){
            showErrorMessage(context,context.resources.getString(resourceId))
        }

    }
}
