package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.resmodels.GenerateTokenRes
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getJsonObject
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.Channel
import java.lang.reflect.Type

internal class GenerateTokenResDs : JsonDeserializer<GenerateTokenRes> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): GenerateTokenRes {
        val gson = GsonUtils.gson

        val generateTokenRes = GenerateTokenRes()

        val jsonObject = json.asJsonObject


        jsonObject.getJsonObject("Data")?.let { dataObject ->

            generateTokenRes.pseudoName = dataObject.getString("pseudoName") ?: ""
            generateTokenRes.authToken = dataObject.getString("authToken") ?: ""

            dataObject.getJsonObject("channel")?.let { it ->
                generateTokenRes.channel = gson.fromJson(it, Channel::class.java)
            }

        }
        return generateTokenRes
    }
}