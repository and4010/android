package com.biotech.framework.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by KNightING on 2018/10/17
 */

object DateUtil {

    @JvmStatic
    fun getString(dateEnum: DateEnum, locale: Locale = Locale.getDefault()): String {
        return SimpleDateFormat(dateEnum.format, locale).format(Date())
    }

    @JvmStatic
    fun getString(format: String, locale: Locale = Locale.getDefault()): String {
        return SimpleDateFormat(format, locale).format(Date())
    }

    @JvmStatic
    fun getString(date: Date, dateEnum: DateEnum, locale: Locale = Locale.getDefault()): String {
        return SimpleDateFormat(dateEnum.format, locale).format(date)
    }

    @JvmStatic
    fun getString(date: Date, format: String, locale: Locale = Locale.getDefault()): String {
        return SimpleDateFormat(format, locale).format(date)
    }

    @JvmStatic
    fun getLong(long: Long, dateEnum: DateEnum, locale: Locale = Locale.getDefault()):String{
        return SimpleDateFormat(dateEnum.format, locale).format(long)
    }

    @JvmStatic
    fun getDate(origin: String, dateEnum: DateEnum, locale: Locale = Locale.getDefault()): Date {
        return SimpleDateFormat(dateEnum.format, locale).parse(origin)
    }

    @JvmStatic
    fun getDate(origin: String, format: String, locale: Locale = Locale.getDefault()): Date {
        return SimpleDateFormat(format, locale).parse(origin)
    }

    @JvmStatic
    fun differToSec(one: Date, two: Date = Date()): Long {
        return ((one.time / 1000) - (two.time / 1000)) * -1
    }

    @JvmStatic
    fun differToMin(one: Date, two: Date = Date()): Int {
        return (((one.time / 1000 / 60) - (two.time / 1000 / 60)).toInt()) * -1
    }

    //秒(毫秒)
    const val ONE_SECOUND: Long = 1000

    //分鐘(毫秒)
    const val ONE_MINUTE = 60 * ONE_SECOUND

    //小時(毫秒)
    const val ONE_HOUR = 60 * ONE_MINUTE

    //天(毫秒)
    const val ONE_DAY = 24 * ONE_HOUR

    enum class DateEnum(val format: String) {
        /**
         * MMdd
         */
        Md("MMdd"),
        /**
         * yyyyMMdd
         */
        D("yyyyMMdd"),
        /**
         * yyyy-MM-dd
         */
        DByHyphen("yyyy-MM-dd"),
        /**
         * yyyy_MM_dd
         */
        DByUnderLine("yyyy_MM_dd"),
        /**
         * day format common
         * yyyy/MM/dd
         */
        DC("yyyy/MM/dd"),
        /**
         * HHmmss
         */
        T("HHmmss"),
        /**
         * SUBTIMEPERIOD_ID format common
         * HH:mm:ss
         */
        TC("HH:mm:ss"),
        /**
         * yyyyMMddHHmmss
         */
        DT("yyyyMMddHHmmss"),
        /**
         * yyyy/MM/dd/HH/mm/ss
         */
        DTBySlash("yyyy/MM/dd/HH/mm/ss"),
        /**
         * yyyy/MM/dd HH:mm:ss
         */
        DCWithTC("yyyy/MM/dd HH:mm:ss"),
        /**
         * yyyy-MM-dd HH:mm:ss
         */
        DByHyphenTC("yyyy-MM-dd HH:mm:ss"),
        /**
         * yyyy_MM_dd HH:mm:ss
         */
        DByUnderLineTC("yyyy_MM_dd HH:mm:ss"),
        /**
         * yyyyMMddHHmmssSS
         */
        DT16("yyyyMMddHHmmssSS"),
        /**
         * yyyyMMddHHmmssSSS
         */
        DT17("yyyyMMddHHmmssSSS"),
        /**
         * yyyyMMddHHmmssSSS
         */
        Y("yyyy"),

        DCWithT("yyyy/MM/dd HH:mm"),
    }
}
