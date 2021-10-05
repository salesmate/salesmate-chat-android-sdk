package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.EmojiMapping
import java.lang.reflect.Type

internal class EmojiMappingDs : JsonDeserializer<EmojiMapping> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): EmojiMapping {

        val emojiMapping = EmojiMapping()

        val jsonObject = json.asJsonObject

        emojiMapping.id = jsonObject.getString("id") ?: ""
        emojiMapping.label = jsonObject.getString("label") ?: ""
        emojiMapping.unicode = jsonObject.getString("unicode") ?: ""

        return emojiMapping
    }
}