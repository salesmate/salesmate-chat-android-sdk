package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.Location
import java.lang.reflect.Type

internal class LocationDs : JsonDeserializer<Location> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Location {

        val location = Location()

        val jsonObject = json.asJsonObject

        location.city = jsonObject.getString("city") ?: ""
        location.country = jsonObject.getString("country") ?: ""
        location.timezone = jsonObject.getString("timezone") ?: ""

        return location
    }
}