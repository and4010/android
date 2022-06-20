package com.biotech.framework.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log

object CheckNetworkUtils {
    fun IsNetworkAvailable(context: Context?): Boolean {
        return if (context == null) false else try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager == null) {
                Log.i("IsNetworkAvailable", "ConnectivityManager is null")
                return false
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities == null) {
                    Log.i("IsNetworkAvailable", "NetworkCapabilities is null")
                    return false
                }
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    true
                } else {
                    Log.i("IsNetworkAvailable", "Unknown NetworkCapabilities")
                    false
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo == null) {
                    Log.i("IsNetworkAvailable", "NetworkInfo is null")
                    return false
                }
                if (activeNetworkInfo.isConnected) {
                    true
                } else {
                    Log.i("IsNetworkAvailable", "NetworkInfo Not Connected")
                    false
                }
            }
        } catch (e: Exception) {
            Log.i("IsNetworkAvailable", "Exception:" + e.message)
            false
        }
    }

    fun IsConnectedToThisServer(host: String): Boolean {
        val runtime = Runtime.getRuntime()
        return try {
            val ipProcess = runtime.exec("/system/bin/ping -c 4 $host")
            val exitValue = ipProcess.waitFor()
            exitValue == 0
        } catch (e: Exception) {
            Log.e("IsConnectedToThisServer", "Exception:" + e.message)
            false
        }
    }

    fun CheckConnectivity(context: Context?): Status {
        return if (IsNetworkAvailable(context)) {
            Status.NormalConnection
        } else {
            Status.AbnormalConnection
        }
    }

    fun CheckConnectivityWithPing(context: Context?, hostDomainName: String): Status {
        return if (!IsNetworkAvailable(context)) {
            Status.AbnormalConnection
        } else if (IsConnectedToThisServer(hostDomainName)) {
            Status.NormalConnection
        } else {
            Status.AbnormalConnection
        }
    }

    enum class Status {
        NormalConnection,  //連線正常
        AbnormalConnection,  //連線異常
        StopCheckConnection //停止檢查連線
    }
}