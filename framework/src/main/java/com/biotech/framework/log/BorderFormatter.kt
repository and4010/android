package com.biotech.framework.log

import com.elvishew.xlog.formatter.border.DefaultBorderFormatter
import com.elvishew.xlog.internal.SystemCompat

/**************************************************************
 * Log外框格式類別
 **************************************************************/
class BorderFormatter : DefaultBorderFormatter() {
    /**************************************************************
     * 內容外框格式化方法
     * @param argSegments 原始Log
     * @return 加上框架後Log
     **************************************************************/


    override fun format(argSegments: Array<String?>?): String? {
        var formatted = super.format(argSegments)
        if (formatted.isNotEmpty()) {
            formatted = SystemCompat.lineSeparator + formatted
        }
        return formatted
    }



}