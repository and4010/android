package com.biotech.framework.log

import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator

class CombinedFileNameGenerator(val Name: String, private val Category: String) :DateFileNameGenerator(){

    override fun generateFileName(logLevel: Int, timestamp: Long): String {
        return Name + "_" + Category + "_" + super.generateFileName(logLevel, timestamp)
    }

}