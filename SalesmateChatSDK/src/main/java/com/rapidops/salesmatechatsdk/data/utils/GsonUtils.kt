package com.rapidops.salesmatechatsdk.data.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonUtils {

    val gson: Gson by lazy {
        val gsonBuilder = GsonBuilder()
        registerTypeAdapters(gsonBuilder)
        gsonBuilder.create()
    }

    private fun registerTypeAdapters(gsonBuilder: GsonBuilder) {

    }
}
