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
            val jsonObject = json.asJsonObject
            jsonObject.getAsJsonObject("Error")?.let { errorJsonObj ->
                if (errorJsonObj.hasProperty("Code")) {
                    apiErrorCode = errorJsonObj.get("Code").asInt
                }
                if (errorJsonObj.hasProperty("code")) {
                    apiErrorCode = errorJsonObj.get("code").asInt
                }
                if (errorJsonObj.hasProperty("Name")) {
                    errorName = errorJsonObj.get("Name").asString
                }
                if (errorJsonObj.hasProperty("name")) {
                    errorName = errorJsonObj.get("name").asString
                }
                if (errorJsonObj.hasProperty("Message")) {
                    errorMessage = errorJsonObj.get("Message").asString
                }
                if (errorJsonObj.hasProperty("message")) {
                    errorMessage = errorJsonObj.get("message").asString
                }
            }
        }
        return Error(
            apiErrorCode,
            errorName,
            errorMessage
        )
    }
}