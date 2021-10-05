package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.data.utils.*
import com.rapidops.salesmatechatsdk.domain.models.*
import java.lang.reflect.Type

internal class PingDs : JsonDeserializer<PingRes> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): PingRes {
        val gson = GsonUtils.gson

        val pingRes = PingRes()

        val jsonObject = json.asJsonObject


        jsonObject.getJsonObject("Data")?.let { dataObject ->

            pingRes.linkname = dataObject.getString("linkname") ?: ""

            pingRes.canVisitorOrContactStartNewConversation =
                dataObject.getBoolean("canVisitorOrContactStartNewConversation")

            dataObject.getJsonObject("availability")?.let {
                pingRes.availability = gson.fromJson(it, Availability::class.java)
            }

            dataObject.getJsonObject("upfrontEmailCollection")?.let {
                pingRes.upfrontEmailCollection =
                    UpfrontEmailCollection(it.getString("frequency") ?: "")
            }

            dataObject.getJsonObject("lookAndFeel")?.let {
                pingRes.lookAndFeel = gson.fromJson(it, LookAndFeel::class.java)
            }

            dataObject.getJsonObject("misc")?.let {
                pingRes.misc = gson.fromJson(it, Misc::class.java)
            }

            dataObject.getJsonObject("conversationsSettings")?.let {
                pingRes.conversationsSettings = gson.fromJson(it, ConversationsSettings::class.java)
            }

            dataObject.getJsonArray("welcomeMessages")?.let {
                val listType = object : TypeToken<List<OfficeHour>>() {}.type
                pingRes.welcomeMessages =
                    gson.fromJson(it, listType)
            }

            dataObject.getJsonObject("securitySettings")?.let {
                pingRes.securitySettings = gson.fromJson(it, SecuritySettings::class.java)
            }

            dataObject.getJsonObject("workspaceData")?.let {
                pingRes.workspaceData = gson.fromJson(it, WorkspaceData::class.java)
            }

            dataObject.getJsonArray("users")?.let {
                val listType = object : TypeToken<List<User>>() {}.type
                pingRes.users =
                    gson.fromJson(it, listType)
            }

            dataObject.getJsonArray("unReadConversations")?.let {
                val listType = object : TypeToken<List<Any>>() {}.type
                pingRes.unReadConversations = gson.fromJson(it, listType)
            }

            dataObject.getJsonArray("contactData")?.let {
                pingRes.contactData = gson.fromJson(it, Any::class.java)
            }

            dataObject.getJsonArray("emojiMapping")?.let {
                val listType = object : TypeToken<List<EmojiMapping>>() {}.type
                pingRes.emojiMapping =
                    gson.fromJson(it, listType)
            }


        }
        return pingRes
    }
}