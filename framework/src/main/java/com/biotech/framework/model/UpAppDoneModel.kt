package com.biotech.framework.model

import com.google.gson.GsonBuilder
import java.io.File

class UpAppDoneModel(val code : Int, val message: String) {

    companion object {
        const val CODE_SUCCESS = 0
        const val CODE_APP_PATH_NOT_EXISTS = 1
        const val CODE_TIMEOUT = 2
        const val CODE_USER_CANCEL = 3
        const val FILENAME = "DONE.json"

        fun read(path : String) : UpAppDoneModel? {
            try {
                File(path).let {
                    if (it.exists()) {
                        return GsonBuilder().create().fromJson(it.readText(), UpAppDoneModel::class.java)
                    }
                }
            }catch (ex : Exception) {

            }
            return null
        }
    }
}