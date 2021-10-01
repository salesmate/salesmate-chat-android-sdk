package com.rapidops.salesmatechatsdk.data.utils

import android.content.Context
import android.content.SharedPreferences

object Prefs {

    private lateinit var preferences: SharedPreferences

    fun initPreferences(context: Context, prefName: String, mode: Int) {
        preferences = context.getSharedPreferences(prefName, mode)
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun putInt(key: String, value: Int) {
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun putLong(key: String, value: Long) {
        val editor = preferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun putFloat(key: String, value: Float) {
        val editor = preferences.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun putString(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getBoolean(key: String, defValue: Boolean = false) = preferences.getBoolean(key, defValue)

    fun getInt(key: String, defValue: Int = 0) = preferences.getInt(key, defValue)

    fun getLong(key: String, defValue: Long = 0L) = preferences.getLong(key, defValue)

    fun getFloat(key: String, defValue: Float = 0.0F) = preferences.getFloat(key, defValue)

    fun getString(key: String, defValue: String? = null) = preferences.getString(key, defValue)

}