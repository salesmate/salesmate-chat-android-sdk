package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.ChannelsItem
import java.lang.reflect.Type

internal class ChannelsItemDs : JsonDeserializer<ChannelsItem> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ChannelsItem {

        val channelsItem = ChannelsItem()

        val jsonObject = json.asJsonObject

        channelsItem.tenantSpecificChannelNameForWidget = jsonObject.getString("tenantSpecificChannelNameForWidget") ?: ""
        channelsItem.contactUnVerifiedChannelName = jsonObject.getString("contactUnVerifiedChannelName") ?: ""

        return channelsItem
    }
}