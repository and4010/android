package com.biotech.framework.util

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.Charset


class PathUtil (val context: Context)  {

    private val TAG = this.javaClass.simpleName

    enum class FolderType(val folder : String) {
        Master("master"),
        Download("download"),
        Upload("upload"),
        Database("database"),
        Log("log"),
        Backup("backup"),
        Root("")
    }

    fun getExternalFile(folderType: FolderType, fileName: String) = File(getExternalFolder(folderType).absolutePath + File.separator + fileName)

    fun getExternalFolder(folderType: FolderType) = File(context.getExternalFilesDir(folderType.folder.toLowerCase())!!.toURI())

    fun createExternalFolders() {
        FolderType.values().forEach {
            val folder = getExternalFolder(it)
            if (!folder.exists())
                folder.mkdir()
        }
    }

    fun getExternalStorageFolder() =  getExternalStorageDirectory()


    fun write(path : String, content : String) : Boolean {
        try {
            val file = File(path)
            FileUtils.write(file, content, Charset.defaultCharset())
            return true
        } catch (e: java.lang.Exception) {

        }

        return false
    }

    fun delete(path : String) {
        try {

            File(path).run {
                if (exists()) {
                    FileUtils.forceDelete(this)
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, ex.message!!)
        }
    }

    fun copy(srcPath : String, dstPath : String) : Boolean {

        val srcFile = File(srcPath)
        val dstFile = File(dstPath)
        try {
            if (!srcFile.exists()) {
                return false
            }

            FileUtils.copyFile(srcFile, dstFile)
            return dstFile.exists()
        } catch (e: java.lang.Exception) {

        }
        return false

    }

    fun move(srcPath : String, dstPath : String) : Boolean {

        val srcFile = File(srcPath)
        val dstFile = File(dstPath)
        try {
            if (!srcFile.exists()) {
                return false
            }

            FileUtils.moveFile(srcFile, dstFile)
            return dstFile.exists()
        } catch (e: java.lang.Exception) {

        }
        return false

    }

    /**
     * 掃描記憶卡檔案，讓電腦看的到最新資料
     */
    fun mediaScan(storageDir : String) {
        File(storageDir).let {
            if (it.isDirectory) {
                Log.i(TAG, "directory:${it.canonicalPath}")
                val files = it.listFiles()
                for (i in 0 until files.size) {
                    mediaScan(files[i].absolutePath)
                }
            } else {
                Log.i(TAG, "file:${it.canonicalPath}")
                scanFile(it.absolutePath)
            }
        }
    }


    /**
     * 掃描記憶卡檔案，讓電腦看的到最新資料
     */
    private fun scanFile(storageFile : String) {
        MediaScannerConnection.scanFile(context, arrayOf(storageFile), null,
            object : MediaScannerConnection.OnScanCompletedListener {
                override fun onScanCompleted(path: String?, uri: Uri?) {
                }
            })
    }

}