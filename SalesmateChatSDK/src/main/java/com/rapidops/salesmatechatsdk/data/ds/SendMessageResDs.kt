package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.resmodels.SendMessageRes
import com.rapidops.salesmatechatsdk.data.utils.getString
import java.lang.reflect.Type

internal class SendMessageResDs : JsonDeserializer<SendMessageRes> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): SendMessageRes {

        val sendMessageRes = SendMessageRes()

        val jsonObject = json.asJsonObject

        sendMessageRes.status = jsonObject.getString("Status") ?: ""

        /*jsonObject.getJsonObject("Data")?.let { dataObject ->

        }*/
        return sendMessageRes
    }
}