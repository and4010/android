package com.biotech.framework.model

data class Response<T> (
    val processID: String = "",
    var code: Int = 0,
    var msg: String = "",
    val size: Long = 0L,
    val md5: String = "",
    var data: T? = null
)