package com.biotech.framework.log

import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy

class BackupStrategy(maxSize: Long) : FileSizeBackupStrategy(maxSize) {



}