package com.biotech.framework.model


data class KeyValueModel(
        val key : String,
        val value : String
) {
    override fun toString(): String {
        return key
    }
}