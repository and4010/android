package com.biotech.framework.extension

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class Preference<T>(val context: Context,val name:String,val default:T):ReadWriteProperty<Any?,T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name,default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        return  putPreferences(name,value)
    }

    val prefs : SharedPreferences by lazy { context.getSharedPreferences("Default",Context.MODE_PRIVATE) }

    private fun <T> findPreference(name:String,default: T) :T = with(prefs){
        val res : Any = when(default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)!!
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalAccessException("this type can be saved into Preference")
        }
        return res as T
    }

    private fun <T> putPreferences (name: String,value: T) = with(prefs.edit()){
        when (value){
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalAccessException("this type can be saved into Preference")
        }.apply()
    }

    /** * 删除全部 資料*/
    fun clearPreference(){
        prefs.edit().clear().apply()
    }

    /** * 根據key删除存储資料 */
    fun clearPreference(key : String){
        prefs.edit().remove(key).apply()
    }
}