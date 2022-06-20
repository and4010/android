package com.user.example.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.biotech.framework.dialog.LoadingDialog
import com.biotech.framework.model.UsbActionModel
import com.biotech.framework.model.UsbDoneModel
import com.biotech.framework.util.PathUtil
import com.biotech.framework.util.UsbUtil
import com.biotech.framework.template.BaseFragment
import com.user.example.R
import com.user.example.StartApplication
import java.io.File


class UsbTransferFragment : BaseFragment(R.layout.fragment_usb_transfer) {
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        @JvmStatic
        fun getNewInstance(title: String): UsbTransferFragment {
            return UsbTransferFragment().apply { this.title = title }
        }
    }
    override val TAG : String = UsbTransferFragment::class.java.simpleName
    private val btnUploadFile by bindView<Button>(R.id.btnUploadFile, true)
    private val btnDownloadFile by bindView<Button>(R.id.btnDownloadFile, true)
    private val txtCodeValue by bindView<TextView>(R.id.txtCodeValue, false)
    private val txtMessageValue by bindView<TextView>(R.id.txtMessageValue, false)

    override fun initView() {
        btnDownloadFile
        btnUploadFile
    }

    override fun initEvent() {
        //TODO 點擊事件

    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                btnDownloadFile.id -> {
                    PathUtil(context!!) .let { fileUtil ->


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
                        val downloadPath =
                            fileUtil.getExternalFolder(PathUtil.FolderType.Download)
                                .absolutePath.removePrefix(fileUtil.getExternalFolder(PathUtil.FolderType.Root).absolutePath + File.separator)

                        //app download 下載相對路徑
                        val action = arrayOf(
                            UsbActionModel.Action(UsbActionModel.CODE_DOWNLOAD, 1, "app-debug.apk", downloadPath),
                            UsbActionModel.Action(UsbActionModel.CODE_DOWNLOAD, 2, "app-debug2.apk", downloadPath)
                        )

                        val usbActionModel = UsbActionModel(appPath).apply { this.actions.addAll(action) }

                        //顯示等待視窗
                        val loadingDialog =
                            LoadingDialog.getInstance(requireContext())
                                .setMessage("下載中，請稍候…")
                                .setCancel(true)
                                .setOnCancelListener(DialogInterface.OnCancelListener {
                                    val startPath =
                                        fileUtil.getExternalStorageFolder().absolutePath + File.separator + UsbActionModel.FILENAME
                                    val donePath =
                                        fileUtil.getExternalStorageFolder().absolutePath + File.separator + UsbDoneModel.FILENAME

                                    fileUtil.delete(startPath)
                                    fileUtil.delete(donePath)
                                    //讓檔案變更在電腦端可視
                                    fileUtil.mediaScan(startPath)
                                    fileUtil.mediaScan(donePath)
                                })
                        loadingDialog.show()

                        val completed = { model: UsbDoneModel? ->
                            loadingDialog.dismiss()
                            if (model != null) {
                                when (model.code) {
                                    UsbDoneModel.CODE_SUCCESS -> {
                                        var message = ""
                                        for (i in 0 until usbActionModel.actions.size) {
                                            when (usbActionModel.actions[i].code) {
                                                UsbActionModel.CODE_DOWNLOAD -> {
                                                    val downloadFile =
                                                        fileUtil.getExternalFolder(PathUtil.FolderType.Root).absolutePath + File.separator + usbActionModel.actions[i].path + File.separator + usbActionModel.actions[i].request
                                                    if (!File(downloadFile).exists()) {
                                                        message += "檔案${i}: ${downloadFile}\r\n"
                                                    }
                                                }
                                                else -> {

                                                }
                                            }
                                        }
                                        if (message.isEmpty()) {
                                            txtCodeValue.text = "${model.code}-成功"
                                            txtMessageValue.text = ""
                                        } else {
                                            txtCodeValue.text = "${model.code}-部分檔案不存在"
                                            txtMessageValue.text = message
                                        }
                                    }
                                    UsbDoneModel.CODE_APP_PATH_NOT_EXISTS -> {
                                        txtCodeValue.text = "${model.code}-找不到應用程式目錄"
                                        txtMessageValue.text = ""
                                    }
                                    UsbDoneModel.CODE_TIMEOUT -> {
                                        txtCodeValue.text = "${model.code}-執行時間逾時"
                                        txtMessageValue.text = ""
                                    }
                                    else -> {
                                        txtCodeValue.text = "未知錯誤"
                                        txtMessageValue.text = model.message
                                    }
                                }
                            }
                        }

                        UsbUtil(StartApplication.instance).execute(usbActionModel, 120000, completed)

                    }
                }
                btnUploadFile.id -> {
                  PathUtil(context!!).let { fileUtil ->


                        Log.i(
                            TAG,
                            "root path :${fileUtil.getExternalFolder(PathUtil.FolderType.Root).absolutePath}"
                        )
                        Log.i(
                            TAG,
                            "upload path :${fileUtil.getExternalFolder(PathUtil.FolderType.Upload).absolutePath}"
                        )

                        //app files 相對路徑
                        val appPath = fileUtil.getExternalFolder(PathUtil.FolderType.Root)
                            .absolutePath.removePrefix(fileUtil.getExternalStorageFolder().absolutePath)

                        //upload 相對路徑
                        val uploadPath =
                            fileUtil.getExternalFolder(PathUtil.FolderType.Upload)
                                .absolutePath.removePrefix(fileUtil.getExternalFolder(PathUtil.FolderType.Root).absolutePath + File.separator)

                        val action = arrayOf(
                            UsbActionModel.Action(UsbActionModel.CODE_UPLOAD, 1, "app-debug.apk", uploadPath),
                            UsbActionModel.Action(UsbActionModel.CODE_UPLOAD, 2, "app-debug2.apk", uploadPath)
                        )

                        val usbActionModel = UsbActionModel(appPath).apply { this.actions.addAll(action) }

                        //顯示等待視窗
                        val loadingDialog =
                            LoadingDialog.getInstance(requireContext())
                                .setMessage("上傳中，請稍候…")
                                .setCancel(true)
                                .setOnCancelListener(DialogInterface.OnCancelListener {
                                    val startPath =
                                        fileUtil.getExternalStorageFolder().absolutePath + File.separator + UsbActionModel.FILENAME
                                    val donePath =
                                        fileUtil.getExternalStorageFolder().absolutePath + File.separator + UsbDoneModel.FILENAME

                                    fileUtil.delete(startPath)
                                    fileUtil.delete(donePath)
                                    //讓檔案變更在電腦端可視
                                    fileUtil.mediaScan(startPath)
                                    fileUtil.mediaScan(donePath)
                                })

                        loadingDialog.show()
                        val completed: (model: UsbDoneModel?) -> Unit = { model: UsbDoneModel? ->

                            loadingDialog.dismiss()
                            if (model != null) {
                                when (model.code) {
                                    UsbDoneModel.CODE_SUCCESS -> {
                                        txtCodeValue.text = "${model.code}-成功"
                                        txtMessageValue.text = ""
                                    }
                                    UsbDoneModel.CODE_APP_PATH_NOT_EXISTS -> {
                                        txtCodeValue.text = "${model.code}-找不到應用程式目錄"
                                        txtMessageValue.text = ""
                                    }
                                    UsbDoneModel.CODE_TIMEOUT -> {
                                        txtCodeValue.text = "${model.code}-執行時間逾時"
                                        txtMessageValue.text = ""
                                    }
                                    else -> {
                                        txtCodeValue.text = "未知錯誤"
                                        txtMessageValue.text = model.message
                                    }
                                }
                            }
                        }

                        UsbUtil(StartApplication.instance).execute(usbActionModel, 120000, completed)

                    }

                }
                else -> {
                }
            }
        }
    }
}



