package com.biotech.framework.log

import com.biotech.framework.util.DateUtil
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.Logger
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.Flattener2
import com.elvishew.xlog.formatter.message.throwable.DefaultThrowableFormatter
import com.elvishew.xlog.printer.Printer
import com.elvishew.xlog.printer.file.FilePrinter
import java.util.*

class LogUtil(val path: String, val name: String, val TAG: String) {

    companion object{
        var init = false
    }

    // 設定檔名
    private fun setLogFlatterer(): Flattener2 {
        return Flattener2 { timeMillis, logLevel, tag, message ->
            val str: String = DateUtil.getString(Date(), DateUtil.DateEnum.DByHyphen)
            str + '|' + tag + '|' + LogLevel.getShortLevelName(logLevel) + '|'  + message
        }
    }


   private fun initConfig() {
        val config = LogConfiguration.Builder()
            .borderFormatter(BorderFormatter())
            .tag(TAG) // 指定 TAG，默认为 "X-LOG"
            .logLevel(LogLevel.ALL)
            .throwableFormatter(DefaultThrowableFormatter()) // Default: DefaultThrowableFormatter
            .build()
        XLog.init(config)
    }


    fun InfoLog(): Logger? {
        if (!init) {
            initConfig()
        }
        val filePrinter: Printer = FilePrinter.Builder(path) // 指定保存日志文件的路径
            .fileNameGenerator(CombinedFileNameGenerator(name, "Log"))
            .flattener(setLogFlatterer())
            .build()
        init = true
        return XLog.tag(TAG).printers(filePrinter).build()
    }


    fun ErrorLog(): Logger? {
        val ErrorPrinter: Printer = FilePrinter.Builder(path) // 指定保存日志文件的路径
            .fileNameGenerator(CombinedFileNameGenerator(name, "ErrorLog"))
            .flattener(setLogFlatterer())
            .build()
        return XLog
            .tag(TAG)
            .enableBorder()
            .enableThreadInfo() // 允许打印线程信息，默认禁止
            .enableStackTrace(5) // 允许打印深度为2的调用栈信息，默认禁止
            .printers(ErrorPrinter)
            .build()
    }


    fun NetWorkLog(): Logger? {
        val NetWorkPrinter: Printer = FilePrinter.Builder(path) // 指定保存日志文件的路径
            .fileNameGenerator(CombinedFileNameGenerator(name, "NetWorkLog"))
            .flattener(setLogFlatterer()) //設定最大2MB
            .backupStrategy(BackupStrategy(2048 * 2048))
            .build()
        return XLog
            .tag(TAG)
            .printers(NetWorkPrinter)
            .build()
    }
}