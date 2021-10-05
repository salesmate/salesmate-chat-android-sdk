package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getBoolean
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.WelcomeMessage
import java.lang.reflect.Type

internal class WelcomeMessagesDs : JsonDeserializer<WelcomeMessage> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): WelcomeMessage {

        val welcomeMessage = WelcomeMessage()

        val jsonObject = json.asJsonObject
        welcomeMessage.language = jsonObject.getString("language") ?: ""
        welcomeMessage.greetingMessage = jsonObject.getString("greeting_message") ?: ""
        welcomeMessage.teamIntro = jsonObject.getString("team_intro") ?: ""
        welcomeMessage.isDefault = jsonObject.getBoolean("is_default")

        return welcomeMessage
    }
}