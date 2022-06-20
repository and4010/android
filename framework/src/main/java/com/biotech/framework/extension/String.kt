package com.biotech.framework.extension

import android.util.Patterns

fun String.isValidEmail(): Boolean
        = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPhone(): Boolean
        = this.isNotEmpty() &&
        Patterns.PHONE.matcher(this).matches()

fun String.isValidTwPhone(): Boolean
        = this.isNotEmpty() &&
        Regex("^[09]{2}[0-9]{8}$").matches(this)


fun String.isValidMemberName(validator: (() -> Boolean)? = null): Boolean
        = this.isNotEmpty() && validator?.invoke()?:true


/**************************************************************
 * 判斷是否為網路位址 IPv4
 * @param
 * @return
 **************************************************************/
fun String.isIPv4Address() : Boolean
= this.isNotEmpty() && Regex("^((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\\\" +
        ".(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\\\" +
        ".(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\\\" +
        ".(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9]))\$").matches(this)

