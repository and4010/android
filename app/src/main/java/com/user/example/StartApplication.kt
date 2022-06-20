package com.user.example

import android.app.Application
import com.biotech.framework.log.LogUtil
import com.biotech.framework.util.PathUtil
import com.user.example.database.AppDataBase
import kotlin.properties.Delegates

class StartApplication : Application() {

    companion object {
        var instance by Delegates.notNull<StartApplication>()
    }

    lateinit var db: AppDataBase

    val fileUtil by lazy { PathUtil(instance) }

    val preferencePath by lazy { fileUtil.getExternalFile(PathUtil.FolderType.Database,"Preferences.db").absolutePath!! }

    val log by lazy { LogUtil(fileUtil.getExternalFile(PathUtil.FolderType.Log,"Log").absolutePath,
        "Log","").InfoLog()
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        //建立程式環境資料夾
        fileUtil.createExternalFolders()
        //建立資料庫
        db = AppDataBase.getInstance(this, preferencePath)

    }

}