package com.user.example.repository

import android.content.Context
import com.biotech.framework.model.Response
import com.biotech.framework.model.ResultModel
import com.biotech.framework.util.MD5
import com.biotech.framework.util.PathUtil
import com.biotech.framework.util.SocketUtil
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.user.example.StartApplication
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.*


class SocketRepository(var context: Context) {


    var ipAddress: String? = null
    var port = 23000
    var timeout = 1000



    companion object {

        const val Common = "000"
        const val Login  = "001"
        const val JsonMsg = "002"
        const val DowLoad = "003"
        const val UpData = "004"

        const val DownloadHeaderProcessIdLength = 3
        const val DownloadHeaderCodeLength = 4
        const val DownloadHeaderMsgLength = 100
        const val DownloadHeaderMd5Length = 32
        const val DownloadHeaderSizeLength = 8
        const val DownloadHeaderTotalLength = DownloadHeaderProcessIdLength + DownloadHeaderCodeLength + DownloadHeaderMsgLength + DownloadHeaderMd5Length + DownloadHeaderSizeLength

    }


    fun request() : Response<String> {
        val json = Gson().toJson("request")
        val content = json.toByteArray(charset("UTF8"))
        val byteBuffer = generatePackageForTypeA(Common, "device", content)
        val response = ByteBuffer.wrap(connect(byteBuffer.array())!!)
        val t : Class<String>? = null
        return deserialize(response,t!!)
    }

    private fun connect(data: ByteArray) : ByteArray?{
        val response : ByteArray? = null
        try {
            val socketUtil = SocketUtil()
            socketUtil.ipAddress = ipAddress
            socketUtil.port = port
            socketUtil.timeout = timeout
            socketUtil.connect()
            socketUtil.send(data)
            socketUtil.waitForResponse()
            socketUtil.close()
        }catch (e: java.lang.Exception){
            e.printStackTrace();
        }
        return response
    }


    private fun <T> deserialize(byteBuffer: ByteBuffer?, tClass: Class<T>): Response<T> {
        val response: Response<ByteArray> = deserialize(byteBuffer!!)
        val result: Response<T> = Response(
            response.processID,
            response.code,
            response.msg,
            response.size,
            response.md5
        )
        if (tClass.simpleName.equals("File", ignoreCase = true)) {
            val file: File = StartApplication.instance.fileUtil.getExternalFile(
                PathUtil.FolderType.Download,
                UUID.randomUUID().toString() + ".apk"
            )
            val fileResultModel: ResultModel<File> = saveFile(file, response.data!!, result.md5)
            if (fileResultModel.code === 0) {
                result.data = fileResultModel.data as T?
            }
        } else {
            result.data = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
                .fromJson(String(response.data!!), tClass)
        }
        return result
    }


    private fun deserialize(byteBuffer: ByteBuffer): Response<ByteArray>{
        val processCodeBytes = ByteArray(3)
        val codeBytes = ByteArray(4)
        val messageBytes = ByteArray(500)
        val md5Bytes = ByteArray(32)
        byteBuffer[processCodeBytes]
        byteBuffer[codeBytes]
        byteBuffer[messageBytes]
        val length = byteBuffer.long
        byteBuffer[md5Bytes]
        val processCode = String(processCodeBytes)
        val code = String(codeBytes).toInt()
        val message = String(messageBytes).trim { it <= ' ' }
        val md5 = String(md5Bytes).trim { it <= ' ' }
        val remaining = ByteArray(byteBuffer.remaining())
        byteBuffer[remaining, 0, remaining.size]
        val response = Response<ByteArray>(processCode, code, message, length, md5)
        return response
    }


    private fun saveFile(file: File, data: ByteArray, checkSum: String) : ResultModel<File>{
        val model: ResultModel<File> = ResultModel()

        try {

            if (data.isEmpty()) {
                model.code = -1
                model.msg = file.toString() + "驗證碼錯誤!!"
                return model
            }

            val bufferedOutputStream = BufferedOutputStream(FileOutputStream(file))
            bufferedOutputStream.write(data)
            bufferedOutputStream.flush()
            bufferedOutputStream.close()

            if(!MD5().CheckMD5(checkSum, file)){
                StartApplication.instance.fileUtil.delete(file.absolutePath)
            }


        }catch (e: Exception){
            e.printStackTrace()
            model.code = -1
            model.msg = "檔案寫入" + e.message
            model.data = null
        }
        return model
    }

    private fun generatePackageForTypeA(processID: String, deviceNo: String, content: ByteArray) : ByteBuffer{

        val byteBuffer = ByteBuffer.allocate(DownloadHeaderTotalLength)
        byteBuffer.put("A".toByteArray())
        byteBuffer.put(processID.toByteArray())
        byteBuffer.put(deviceNo.toByteArray())
        byteBuffer.putLong(content.size.toLong())
        byteBuffer.put(content)

        return byteBuffer
    }

    private fun generatePackageForTypeB(
        processID: String,
        deviceNo: String,
        file: File,
        content: ByteArray
    ) : ByteBuffer{

        val byteBuffer = ByteBuffer.allocate(3 + 25 + 5 + 8 + content.size + 100 + 32)
        byteBuffer.put("B".toByteArray())
        byteBuffer.put(processID.toByteArray())
        byteBuffer.put(deviceNo.toByteArray())
        byteBuffer.putLong(content.size.toLong())
        byteBuffer.put(file.name.toByteArray())
        byteBuffer.put(MD5().calculateMD5(file)!!.toByteArray())

        byteBuffer.put(content)
        return byteBuffer
    }


}