package com.biotech.framework.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by KNightING on 2018/10/23
 */

class ScheduleService
    : Service() {

    private val binder by lazy { ScheduleService.ScheduleServiceBinder(this) }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    private data class TimerData(
            var timer: Timer,
            val runnable: Runnable,
            var loopSecond: Long = 0,
            var isFixRate: Boolean = false)

    private val scheduleList by lazy { mutableMapOf<String, TimerData>() }

    private fun generateTag(): String {
        var tag = ""
        while (tag.isEmpty() || scheduleList[tag] != null)
            tag = SimpleDateFormat("yyMMddHHmmssSSS", Locale.getDefault()).format(Date())
        return tag
    }

    /**
     *  add new schedule
     *
     *  @param runnable work something in timerTask Thread, don't do UI control without runOnUiThread.
     *  @param delay run first SUBTIMEPERIOD_ID after delay(ms) SUBTIMEPERIOD_ID.
     *  @param loopSecond when it smaller 0 than be  a one SUBTIMEPERIOD_ID schedule, and default is -1
     *
     * @return return schedule's auto generate tag
     */
    fun addSchedule(runnable: Runnable, delay: Long, loopSecond: Long = -1, isFixRate: Boolean = false): String {
        val tag = generateTag()
        val timer = getScheduleTimer(runnable, delay, loopSecond, isFixRate)
        scheduleList[tag] = TimerData(timer, runnable, loopSecond, isFixRate)
        return tag
    }

    /**
     * add job to schedule and wait user to call [runSchedule]
     */
    fun addLazySchedule(runnable: Runnable, loopSecond: Long = -1, isFixRate: Boolean = false): String {
        val tag = generateTag()
        scheduleList[tag] = TimerData(Timer(), runnable, loopSecond, isFixRate)
        return tag
    }

    /**
     *  add new schedule
     *
     *  @param runnable work something in timerTask Thread, don't do UI control without runOnUiThread.
     *  @param date run first SUBTIMEPERIOD_ID at the date.
     *  @param loopSecond when it smaller 0 than be  a one SUBTIMEPERIOD_ID schedule, and default is -1
     *
     * @return return schedule's auto generate tag
     */
    fun addSchedule(runnable: Runnable, date: Date, loopSecond: Long = -1, isFixRate: Boolean = false): String {
        val tag = generateTag()
        val timer = getScheduleTimer(runnable, date, loopSecond, isFixRate)
        scheduleList[tag] = TimerData(timer, runnable, loopSecond, isFixRate)
        return tag
    }

    /**
     * run schedule by tag
     *
     * @param tag tag is auto generate when [addSchedule]
     *
     * @return false mean is can't find schedule by tag else it's run after delay(ms) SUBTIMEPERIOD_ID.
     */
    fun runSchedule(tag: String, delay: Long): Boolean {
        val timeData = scheduleList[tag] ?: return false
        timeData.timer.cancel()
        timeData.timer.purge()
        val timer = getScheduleTimer(timeData.runnable, delay, timeData.loopSecond, timeData.isFixRate)
        scheduleList[tag] = TimerData(timer, timeData.runnable, timeData.loopSecond, timeData.isFixRate)
        return true
    }

    /**
     * run schedule by tag
     *
     * @param tag tag is auto generate when [addSchedule]
     *
     * @return false mean is can't find schedule by tag else it's run after delay(ms) SUBTIMEPERIOD_ID.
     */
    fun runSchedule(tag: String, date: Date): Boolean {
        val timeData = scheduleList[tag] ?: return false
        timeData.timer.cancel()
        timeData.timer.purge()
        val timer = getScheduleTimer(timeData.runnable, date, timeData.loopSecond, timeData.isFixRate)
        scheduleList[tag] = TimerData(timer, timeData.runnable, timeData.loopSecond, timeData.isFixRate)
        return true
    }

    /**
     * update schedule loop SUBTIMEPERIOD_ID and isFixRate
     *
     * @param immediately is true will do it now else it will update next start
     */
    fun updateSchedule(tag: String, immediately: Boolean = false, delay: Long = 0, loopSecond: Long = 0, isFixRate: Boolean = false): Boolean {
        val timeData = scheduleList[tag] ?: return false
        if (immediately || timeData.loopSecond <= 0) {
            timeData.timer.cancel()
            timeData.timer.purge()
            val timer = getScheduleTimer(timeData.runnable, delay, loopSecond, isFixRate)
            scheduleList[tag] = TimerData(timer, timeData.runnable, loopSecond, isFixRate)
        } else
            scheduleList[tag] = TimerData(timeData.timer, timeData.runnable, loopSecond, isFixRate)
        return true
    }

    fun findTagByRunnableObj(runnable: Runnable): String? {
        return scheduleList.entries.find { it.value.runnable == runnable }?.key
    }

    /**
     * schedule and do it runtime
     */
    private fun getScheduleTimer(runnable: Runnable, delay: Long, loopSecond: Long = 0, isFixRate: Boolean = false): Timer {
        val timerTask = object : TimerTask() {
            override fun run() {
                runnable.run()
            }
        }

        val timer = Timer()
        if (loopSecond > 0) {
            if (isFixRate)
                timer.scheduleAtFixedRate(timerTask, delay, (loopSecond * 1000))
            else
                timer.schedule(timerTask, delay, (loopSecond * 1000))
        } else
            timer.schedule(timerTask, delay)
        return timer
    }

    /**
     * schedule and do it runtime
     */
    private fun getScheduleTimer(runnable: Runnable, date: Date, loopSecond: Long = 0, isFixRate: Boolean = false): Timer {
        val timerTask = object : TimerTask() {
            override fun run() {
                runnable.run()
            }
        }

        val timer = Timer()
        if (loopSecond > 0) {
            if (isFixRate)
                timer.scheduleAtFixedRate(timerTask, date, (loopSecond * 1000))
            else
                timer.schedule(timerTask, date, (loopSecond * 1000))
        } else
            timer.schedule(timerTask, date)
        return timer
    }

    /**
     * cancel schedule by tag
     *
     * @param tag tag is auto generate when [addSchedule]
     */
    fun cancelSchedule(tag: String): Boolean {
        val timeData = scheduleList[tag] ?: return false
        timeData.timer.cancel()
        timeData.timer.purge()
        scheduleList.remove(tag)
        return true
    }

    /**
     * cancel all schedule
     */
    fun cancelAllSchedule() {
        scheduleList.forEach {
            it.value.timer.cancel()
            it.value.timer.purge()
        }
        scheduleList.clear()
    }

    class ScheduleServiceBinder(val service: ScheduleService) : Binder()
}