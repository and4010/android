package com.biotech.framework.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import java.io.File

class InstallApk(val context: Context) {

    fun installApk(file: File?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && !context.packageManager.canRequestPackageInstalls()) {
            startInstallPermissionSettingActivity(context)
        }

        //有權限，開始安裝。
        install(file)
    }

    /**
     * 開啟設定安裝未知來源應用許可權介面
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun startInstallPermissionSettingActivity(context: Context?) {
        if (context == null) {
            return
        }
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
        (context as Activity).startActivityForResult(intent, 10086)
    }


   private fun install(file: File?) {
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        } else {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(
                context, context.packageName + ".provider",
                file!!
            )
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        }
        context.startActivity(intent)
    }


}