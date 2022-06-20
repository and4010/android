package com.biotech.framework.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.annotation.RequiresPermission
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Socket
import java.net.URL



/**
 * Network util
 * need to  permissions of the following:
 * android.permission.ACCESS_NETWORK_STATE
 * android.permission.INTERNET
 */
class NetworkUtil(val app: Application) {

    companion object {
        private var instance: NetworkUtil? = null

        fun getInstance(app: Application): NetworkUtil {
            if (instance == null)
                instance = NetworkUtil(app)
            return instance!!
        }

        /**
         * the way can run on main thread, but some device can not work..
         */
        @RequiresPermission(android.Manifest.permission.INTERNET)
        fun checkPing(host: String = "8.8.8.8"): Boolean {
            try {
                val ipProcess = Runtime.getRuntime().exec("/system/bin/ping -c 1 $host")
                val exitValue = ipProcess.waitFor()
                return exitValue == 0
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return false
        }

        /**
         * the way cna not run on main thread, but work on any device.
         */
        @RequiresPermission(android.Manifest.permission.INTERNET)
        fun checkSocket(host: String = "8.8.8.8", port: Int = 53, timeout: Int = 1500): Boolean {
            return try {
                val socket = Socket()
                socket.connect(InetSocketAddress(host, port), timeout)
                socket.close()
                true
            } catch (e: IOException) {
                false
            }
        }

        @RequiresPermission(android.Manifest.permission.INTERNET)
        fun checkUrl(url: String = "https://www.google.com", timeout: Int = 3000): Boolean {
            return try {
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                urlConnection.connectTimeout = timeout
                urlConnection.connect()
                urlConnection.responseCode == HttpURLConnection.HTTP_OK
            } catch (e: Exception) {
                false
            }
        }
    }

    private val TAG by lazy { this::class.java.simpleName }

    private val cm by lazy { app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun isConnected(): Boolean {
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun getCheckOnlineTask(callback: IIsOnlineCallback): CheckOnlineTask {
        return CheckOnlineTask(this, callback)
    }

    interface IIsOnlineCallback {
        fun onPrepare()

        fun onComplete(result: Boolean)
    }

    class CheckOnlineTask(
            private val networkUtil: NetworkUtil,
            private val callback: IIsOnlineCallback)
        : AsyncTask<String, Void, Boolean>() {

        override fun onPreExecute() {
            callback.onPrepare()
        }

        override fun doInBackground(vararg params: String?): Boolean {
            return when {
                !networkUtil.isConnected() -> false
                else -> {
                    val url = params[0]
                    if (url == null)
                        NetworkUtil.checkUrl()
                    else
                        NetworkUtil.checkUrl(url)
                }
            }
        }

        override fun onPostExecute(result: Boolean?) {
            callback.onComplete(result ?: false)
        }
    }
}