package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getBoolean
import com.rapidops.salesmatechatsdk.domain.models.Misc
import java.lang.reflect.Type

internal class MiscDs : JsonDeserializer<Misc> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Misc {

        val misc = Misc()

        val jsonObject = json.asJsonObject

        misc.playSoundsForMessenger = jsonObject.getBoolean("play_sounds_for_messenger")
        misc.gifSupportEnabled = jsonObject.getBoolean("gif_support_enabled")

        return misc
    }
}