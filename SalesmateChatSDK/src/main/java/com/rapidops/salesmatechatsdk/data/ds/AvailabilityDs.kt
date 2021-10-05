package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getJsonArray
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.data.utils.hasPropertyNotNull
import com.rapidops.salesmatechatsdk.domain.models.Availability
import com.rapidops.salesmatechatsdk.domain.models.OfficeHour
import java.lang.reflect.Type

internal class AvailabilityDs : JsonDeserializer<Availability> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Availability {
        val gson = GsonUtils.gson

        val availability = Availability()

        val jsonObject = json.asJsonObject

        availability.replyTime = jsonObject.getString("reply_time") ?: ""
        availability.calculateResponseTimeInOfficeHours =
            jsonObject.get("calculate_response_time_in_office_hours")
        availability.timezone = jsonObject.getString("timezone") ?: ""

        if (jsonObject.hasPropertyNotNull("office_hours")) {
            val listType = object : TypeToken<List<OfficeHour>>() {}.type
            availability.officeHours =
                gson.fromJson(jsonObject.getJsonArray("office_hours"), listType)
        }


        return availability
    }
}