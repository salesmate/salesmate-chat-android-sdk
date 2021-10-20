package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.message.ReferenceTeam
import java.lang.reflect.Type

internal class ReferenceTeamDs : JsonDeserializer<ReferenceTeam> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ReferenceTeam {

        val referenceTeam = ReferenceTeam()

        val jsonObject = json.asJsonObject

        referenceTeam.id = jsonObject.getString("id") ?: ""
        referenceTeam.name = jsonObject.getString("name") ?: ""

        return referenceTeam
    }
}