package com.biotech.framework.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
//import org.slf4j.LoggerFactory

class AlarmService : BroadcastReceiver() {
    companion object {
        var AlarmServiceCallback : ((boolean : Boolean?) -> Unit)? = null
        var interval = 0
        val basetime = 60 * 1000
    }
    val ACTION_PERIOD_SERVICE = "com.frameworks.Service.AlarmService"
    val ACTION_PERIOD_SERVICEDataBase = "com.frameworks.Service.AlarmServiceDataBase"

    private val TAG = this.javaClass.simpleName
//    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)
    var enable = true


    override fun onReceive(p0: Context?, p1: Intent?) {
        try {
            if (p1!!.action == ACTION_PERIOD_SERVICE) {
                schedule(p0!!, p0.getSystemService(Context.ALARM_SERVICE) as AlarmManager)  //再次執行Alarm
                enable = true
                Log.e(TAG,"執行2")
            }
            if (AlarmServiceCallback != null) {
                AlarmServiceCallback!!.invoke(enable) //執行響起後要做的事，由Activity實作
            }
        } catch (e: Exception) {
//            logger.error("$TAG,${String.format("[%s]:%s", "AlarmService", "onReceive:" + e.message)}")
            e.printStackTrace()
        }

    }

    fun schedule(context: Context, alarms: AlarmManager) {
        val totalTime = interval*basetime
        schedule(context, alarms,totalTime)
    }

    fun schedule(context: Context, alarms: AlarmManager, interval: Int) {
        val intent = Intent(context, AlarmService::class.java)
        intent.action = ACTION_PERIOD_SERVICE
        val pi = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarms.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + interval - SystemClock.elapsedRealtime() % 100,
            pi
        )
        Log.e(TAG,"執行")
    }

    fun stopAlarmService(context: Context, alarms: AlarmManager) {
        val intent = Intent(context, AlarmService::class.java)
        intent.action = ACTION_PERIOD_SERVICE
        val pi = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarms.cancel(pi)

    }
}