package com.biotech.framework.model

import com.google.gson.GsonBuilder
import com.biotech.framework.util.PathUtil


class UpAppActionModel(var app_path : String) {

    constructor(app_path: String, vararg actions: Action) : this(app_path) {
        this.actions.addAll(actions)
    }

    companion object {
        const val CODE_DOWNLOAD = 0
        const val FILENAME = "START.json"

        fun write(pathUtil: PathUtil, path : String, actionModel : UpAppActionModel) {
            actionModel.app_path = actionModel.app_path.replace("/", "\\")
            pathUtil.write(
                path,
                GsonBuilder().setPrettyPrinting().create().toJson(actionModel)
            )
        }

    }

    var actions : MutableList<Action> = mutableListOf()

    data class Action (
        val code : Int,
        val request : String,
        val path : String,
        val VersionCode:Int,
        val PackageName:String
    )

}