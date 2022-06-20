package com.biotech.framework.model

open class ResultModel<T> {
    var code = 0
    var msg: String? = null
    var data: T? = null
}