package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.message.ReferenceUser
import java.lang.reflect.Type

internal class ReferenceUserDs : JsonDeserializer<ReferenceUser> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ReferenceUser {

        val referenceUser = ReferenceUser()

        val jsonObject = json.asJsonObject

        referenceUser.id = jsonObject.getString("id") ?: ""
        referenceUser.name = jsonObject.getString("name") ?: ""

        return referenceUser
    }
}