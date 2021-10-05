package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.OfficeHour
import java.lang.reflect.Type

internal class OfficeHoursDs : JsonDeserializer<OfficeHour> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): OfficeHour {

        val officeHour = OfficeHour()

        val jsonObject = json.asJsonObject

        officeHour.startTime = jsonObject.getString("startTime")?:""
        officeHour.endTime = jsonObject.getString("endTime")?:""
        officeHour.weekName = jsonObject.getString("weekName")?:""

        return officeHour
    }
}