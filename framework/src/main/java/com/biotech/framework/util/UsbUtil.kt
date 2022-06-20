package com.biotech.framework.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.biotech.framework.extension.AsyncTask
import com.biotech.framework.model.UsbActionModel
import com.biotech.framework.model.UsbDoneModel

import java.io.File

class UsbUtil(val context : Context) {

    fun execute(usbActionModel : UsbActionModel, timeout : Int = 120000, completed : (done : UsbDoneModel?) -> Unit) {
        PathUtil(context).let { fileUtil ->

            val startPath =
                fileUtil.getExternalStorageFolder().absolutePath + File.separator + UsbActionModel.FILENAME

            val donePath =
                fileUtil.getExternalStorageFolder().absolutePath + File.separator + UsbDoneModel.FILENAME

            //刪除 START.json 及 DONE.json
            fileUtil.delete(startPath)
            fileUtil.delete(donePath)

            //產生START.json
            UsbActionModel.write(fileUtil, startPath, usbActionModel)

            //讓檔案START.json在電腦端可視
            fileUtil.mediaScan(startPath)
            fileUtil.mediaScan(fileUtil.getExternalFolder(PathUtil.FolderType.Root).absolutePath)
            var model: UsbDoneModel? = null

            //顯示下載中訊息
            AsyncTask({
                //等待處理結果

                val starTick = System.currentTimeMillis()
                do {
                    Thread.sleep(100)
                    model = UsbDoneModel.read(donePath)
                    if (model != null) {
                        break
                    }
                    else if(!File(startPath).exists()) {
                        model = UsbDoneModel(UsbDoneModel.CODE_USER_CANCEL, "使用者中止")
                        break
                    }
                    else if(System.currentTimeMillis() - starTick >= timeout) {
                        model = UsbDoneModel(UsbDoneModel.CODE_TIMEOUT, "執行逾時")
                        break
                    }
                } while (true)
            }, {
                //檢查處理結果
                completed(model)

                fileUtil.delete(startPath)
                fileUtil.delete(donePath)
                //讓檔案變更在電腦端可視
                fileUtil.mediaScan(startPath)
                fileUtil.mediaScan(donePath)
                fileUtil.mediaScan(fileUtil.getExternalFolder(PathUtil.FolderType.Root).absolutePath)
            })
        }
    }
}
