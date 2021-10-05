package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getBoolean
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.LookAndFeel
import java.lang.reflect.Type

internal class LookAndFeelDs : JsonDeserializer<LookAndFeel> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LookAndFeel {

        val lookAndFeel = LookAndFeel()

        val jsonObject = json.asJsonObject

        lookAndFeel.backgroundColor = jsonObject.getString("background_color") ?: ""
        lookAndFeel.actionColor = jsonObject.getString("action_color") ?: ""
        lookAndFeel.messengerBackground = jsonObject.getString("messenger_background") ?: ""
        lookAndFeel.logoUrl = jsonObject.getString("logo_url") ?: ""
        lookAndFeel.showPoweredBy = jsonObject.getBoolean("show_powered_by")
        lookAndFeel.launcherPosition = jsonObject.getString("launcher_position") ?: ""
        lookAndFeel.sideSpacing = jsonObject.getString("side_spacing") ?: ""
        lookAndFeel.bottomSpacing = jsonObject.getString("bottom_spacing") ?: ""

        return lookAndFeel
    }
}