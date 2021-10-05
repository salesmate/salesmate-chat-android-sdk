package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.hasProperty
import com.rapidops.salesmatechatsdk.domain.exception.Error
import java.lang.reflect.Type

internal class ErrorDs : JsonDeserializer<Error> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Error {

        var apiErrorCode = 0
        var errorName = ""
        var errorMessage = ""

        if (json!!.isJsonObject) {
            val jsonObject = json.asJsonObject;
            if (jsonObject.hasProperty("Code")) {
                apiErrorCode = jsonObject.get("Code").asInt
            }
            if (jsonObject.hasProperty("code")) {
                apiErrorCode = jsonObject.get("code").asInt
            }
            if (jsonObject.hasProperty("Name")) {
                errorName = jsonObject.get("Name").asString
            }
            if (jsonObject.hasProperty("name")) {
                errorName = jsonObject.get("name").asString
            }
            if (jsonObject.hasProperty("Message")) {
                errorMessage = jsonObject.get("Message").asString
            }
            if (jsonObject.hasProperty("message")) {
                errorName = jsonObject.get("message").asString
            }
        }
        return Error(
            apiErrorCode,
            errorName,
            errorMessage
        )
    }
}