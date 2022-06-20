package com.biotech.framework.util

import android.util.Log
import java.io.*
import java.util.ArrayList
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class ZipUtil {

    private val TAG = this.javaClass.simpleName

    fun Unzip(zipFilePath: String, location: String) {
        try {
            val fin = FileInputStream(zipFilePath)
            Unzip(fin, location)
        } catch (e: Exception) {
            Log.e(TAG, "unzip", e)
        }

    }


    fun Unzip(fin: InputStream, location: String): List<String>? {
        try {
            val zin = ZipInputStream(fin)
            val files = ArrayList<String>()
            val b = ByteArray(1024)

            val ze: ZipEntry = zin.nextEntry
            while (ze != null) {
                Log.v("Decompress", "Unzipping " + ze.name)

                if (ze.isDirectory) {
                    DirChecker(ze.name, location)
                } else {
                    val fout = FileOutputStream(location + ze.name)

                    val input = BufferedInputStream(zin)
                    val output = BufferedOutputStream(fout)

                    var n: Int
                    do{
                        n = input.read(b,0,1024)
                        if (n >=0){
                            output.write(b,0,n)
                        }
                    }while (n>=0)
                    files.add(ze.name)
                    zin.closeEntry()
                    output.close()
                }

            }
            zin.close()
            return files
        } catch (e: Exception) {
            Log.e(TAG, "unzip", e)
            return null
        }

    }

    private fun DirChecker(dir: String, location: String) {
        val f = File(location + dir)

        if (!f.isDirectory) {
            f.mkdirs()
        }
    }



    fun zip(files: Array<File>, zipFile: File) {
        val BUFFER_SIZE = 1024
        var origin: BufferedInputStream? = null
        val out = ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile)))
        try {
            val data = ByteArray(BUFFER_SIZE)

            for (i in files.indices) {
                val fi = FileInputStream(files[i])
                origin = BufferedInputStream(fi, BUFFER_SIZE)
                try {
                    val entry = ZipEntry(files[i].name)
                    out.putNextEntry(entry)
                    var count: Int
                    do {
                        count = origin.read(data,0,BUFFER_SIZE)
                        if (count != -1){
                            out.write(data, 0, count)
                        }
                    }while (count != -1)
                } finally {
                    origin.close()
                }
            }
        } catch (e :Exception){
            Log.e(TAG,e.message!!)
        }finally {
            out.close()
        }
    }

    /**
     * 壓縮檔案和資料夾
     *
     * @param srcFileString 要壓縮的檔案或資料夾
     * @param zipFileString 壓縮完成的Zip路徑
     * @throws Exception
     */
    @Throws(Exception::class)
    fun ZipFolder(srcFileString: String, zipFileString: String) {
        //建立ZIP
        val outZip = ZipOutputStream(FileOutputStream(zipFileString))
        //建立檔案
        val file = File(srcFileString)
        //壓縮
        //        LogUtils.LOGE("---->"+file.getParent()+"==="+file.getAbsolutePath());
        ZipFiles(file.parent + File.separator, file.name, outZip)
        //完成和關閉
        outZip.finish()
        outZip.close()
    }
    /**
     * 壓縮檔案
     *
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun ZipFiles(folderString: String, fileString: String, zipOutputSteam: ZipOutputStream?) {
        //        LogUtils.LOGE("folderString:" + folderString + "\n" +
        //                "fileString:" + fileString + "\n==========================");
        if (zipOutputSteam == null)
            return
        val file = File(folderString + fileString)
        if (file.isFile) {
            val zipEntry = ZipEntry(fileString)
            val inputStream = FileInputStream(file)
            zipOutputSteam.putNextEntry(zipEntry)
            var len: Int
            val buffer = ByteArray(4096)

            do {
                len = inputStream.read(buffer)
                if (len != -1){
                    zipOutputSteam.write(buffer, 0, len)
                }

            }while (len != -1)
            zipOutputSteam.closeEntry()
        } else {
            //資料夾
            val fileList = file.list()
            //沒有子檔案和壓縮
            if (fileList.isEmpty()) {
                val zipEntry = ZipEntry(fileString + File.separator)
                zipOutputSteam.putNextEntry(zipEntry)
                zipOutputSteam.closeEntry()
            }
            //子檔案和遞迴
            for (i in fileList.indices) {
                ZipFiles("$folderString$fileString/", fileList[i], zipOutputSteam)
            }
        }
    }
}