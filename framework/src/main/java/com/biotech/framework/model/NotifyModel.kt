package com.biotech.framework.model

class NotifyModel<T> : ResultModel<T>() {
    var title: String? = null
    var turnOnVibration: Boolean = true
    var turnOnSound: Boolean = true
}