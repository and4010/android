package com.biotech.framework.model

import com.google.gson.GsonBuilder
import com.biotech.framework.util.PathUtil


class UsbActionModel(var app_path : String) {

    companion object {
        const val CODE_DOWNLOAD = 0
        const val CODE_UPLOAD = 1
        const val CODE_DOWNLOAD_ALL = 2
        const val CODE_UPLOAD_ALL = 3
        const val CODE_CHECK_FOR_UPDATE = 4

        const val FILENAME = "START.json"

        fun write(pathUtil: PathUtil, path : String, model : UsbActionModel) {
            model.app_path = model.app_path.replace("/", "\\")
            pathUtil.write(
                path,
                GsonBuilder().setPrettyPrinting().create().toJson(model)
            )
        }

        fun getUpAppRequest(packageName : String, versionCode : Int) = "$packageName;$versionCode;$packageName-new.apk"

        fun getUpAppFilename(request : String) : String {
            val list = request.split(";")
            return if (list.isNotEmpty()) list.get(list.count() - 1) else ""
        }
    }

    constructor(app_path: String, vararg actions: Action) : this(app_path) {
        this.actions.addAll(actions)
    }


    var actions : MutableList<Action> = mutableListOf()

    data class Action (
        val code : Int,
        val order : Int,
        val request : String,
        val path : String
    )




}