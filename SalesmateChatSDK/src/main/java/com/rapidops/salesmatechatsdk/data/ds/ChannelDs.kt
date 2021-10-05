package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getJsonObject
import com.rapidops.salesmatechatsdk.domain.models.Channel
import com.rapidops.salesmatechatsdk.domain.models.ChannelsItem
import com.rapidops.salesmatechatsdk.domain.models.Events
import java.lang.reflect.Type

internal class ChannelDs : JsonDeserializer<Channel> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Channel {
        val gson = GsonUtils.gson

        val channel = Channel()

        val jsonObject = json.asJsonObject


        jsonObject.getJsonObject("channels")?.let {
            channel.channels = gson.fromJson(it, ChannelsItem::class.java)
        }

        jsonObject.getJsonObject("events")?.let {
            channel.events = gson.fromJson(it, Events::class.java)

        }

        return channel
    }
}