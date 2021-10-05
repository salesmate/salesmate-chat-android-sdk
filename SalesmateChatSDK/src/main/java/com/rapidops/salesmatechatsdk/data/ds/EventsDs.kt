package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.Events
import java.lang.reflect.Type

internal class EventsDs : JsonDeserializer<Events> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Events {

        val events = Events()

        val jsonObject = json.asJsonObject

        events.visitorIsTyping = jsonObject.getString("visitorIsTyping") ?: ""
        events.widgetUserPreference = jsonObject.getString("widgetUserPreference") ?: ""

        return events
    }
}