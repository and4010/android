package com.biotech.framework.util

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import com.biotech.framework.dialog.LoadingDialog
import com.biotech.framework.model.UsbActionModel
import com.biotech.framework.model.UsbDoneModel
import java.io.File

class AppUpdataUtils(val context: Context) {

    val TAG: String? = AppUpdataUtils::class.java.simpleName

    fun Start(callback : (message : String) -> Unit){
        PathUtil(context).let { fileUtil ->
            Log.i(
                TAG,
                "root path :${fileUtil.getExternalFolder(PathUtil.FolderType.Root).absolutePath}"
            )
            Log.i(
                TAG,
                "download path :${fileUtil.getExternalFolder(PathUtil.FolderType.Download).absolutePath}"
            )

            //app files 相對路徑
            val appPath = fileUtil.getExternalFolder(PathUtil.FolderType.Root)
                .absolutePath.removePrefix(fileUtil.getExternalStorageFolder().absolutePath)

            //download 相對路徑
            val downloadPath = fileUtil.getExternalFolder(PathUtil.FolderType.Download)
                .absolutePath.removePrefix(fileUtil.getExternalFolder(PathUtil.FolderType.Root).absolutePath + File.separator)

            val packageName  = DeviceInfo(context).packageName
            val packageCode = DeviceInfo(context).versionCode!!.toInt()


            //app download 下載相對路徑
            val action = arrayOf(
                UsbActionModel.Action(UsbActionModel.CODE_CHECK_FOR_UPDATE, 1, UsbActionModel.getUpAppRequest(packageName, packageCode), downloadPath)
            )

            val usbActionModel = UsbActionModel(appPath).apply { actions.addAll(action) }

            val loadingDialog =
                LoadingDialog.getInstance(context)
                    .setMessage("下載中，請稍後...")
                    .setCancel(true)
                    .setOnCancelListener(DialogInterface.OnCancelListener {
                        val startPath =
                            fileUtil.getExternalStorageFolder().absolutePath + File.separator + UsbActionModel.FILENAME
                        val donePath =
                            fileUtil.getExternalStorageFolder().absolutePath + File.separator + UsbDoneModel.FILENAME

                        fileUtil.delete(startPath)
                        fileUtil.delete(donePath)
                        fileUtil.mediaScan(startPath)
                        fileUtil.mediaScan(donePath)
                        fileUtil.mediaScan(downloadPath)

                    })
            loadingDialog.show()

            val completed = {model: UsbDoneModel? ->
                loadingDialog.dismiss()
                if (model != null){
                    when(model.code){
                        UsbDoneModel.CODE_SUCCESS ->{
                            var message = ""
                            for (i in 0 until usbActionModel.actions.size){
                                when(usbActionModel.actions[i].code){
                                    UsbActionModel.CODE_CHECK_FOR_UPDATE -> {
                                        val downloadFile =
                                            fileUtil.getExternalFolder(PathUtil.FolderType.Root).absolutePath + File.separator + usbActionModel.actions[i].path + File.separator + UsbActionModel.getUpAppFilename(
                                                usbActionModel.actions[i].request
                                            )

                                        if (!File(downloadFile).exists()) {
                                            message += "檔案${i}: ${downloadFile}\r\n"
                                        } else {
                                            install(File(downloadFile))
                                        }

                                    }

                                }
                            }
                            if (message.isEmpty()){
                                callback("${model.code}-成功")
                            }else{
                                callback("${model.code}-部分檔案不存在")
                            }
                        }
                        UsbDoneModel.CODE_APP_PATH_NOT_EXISTS ->{
                            callback("${model.code}-找不到應用程式目錄")
                        }
                        UsbDoneModel.CODE_TIMEOUT ->{
                            callback("${model.code}-執行時間逾時")
                        }
                        else ->{
                            callback("${model.message}-未知錯誤")
                        }
                    }
                }
            }

            fileUtil.mediaScan(downloadPath)
            UsbUtil(context).execute(usbActionModel,120000,completed)

        }
    }


    fun checkUpdate(file : File, packagename: String, newVersionCode: Long) {
        if (ckeck(packagename, newVersionCode)) {
            if (file.exists()) {
                install(file)
            }
        }
    }

    fun ckeck(packagename: String, newVersionCode: Long): Boolean {
        if (!checkApplicationId(packagename)) {
            return false
        }
        if (!checkVersionCode(newVersionCode)) {
            return false
        }

        return true
    }


    fun checkApplicationId(ApplicationId: String) = context.packageName == ApplicationId


    fun checkVersionCode(versionCode: Long): Boolean {
        return getVersionCode() >= versionCode
    }

    private fun getVersionCode(): Long = context.packageManager.getPackageInfo(context.packageName, 0).run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            longVersionCode
        } else {
            versionCode.toLong()
        }
    }

    fun install(file : File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = Uri.fromFile(file)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setDataAndType(
            uri,
            "application/vnd.android.package-archive"
        )
        context.startActivity(intent)
    }
}