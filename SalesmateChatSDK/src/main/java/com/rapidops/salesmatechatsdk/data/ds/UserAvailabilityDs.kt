package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getBoolean
import com.rapidops.salesmatechatsdk.data.utils.getJsonArray
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.ContactData
import com.rapidops.salesmatechatsdk.domain.models.Owner
import com.rapidops.salesmatechatsdk.domain.models.UserAvailability
import java.lang.reflect.Type

internal class UserAvailabilityDs : JsonDeserializer<UserAvailability> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): UserAvailability {
        val gson = GsonUtils.gson

        val userAvailability = UserAvailability()

        val jsonObject = json.asJsonObject


        jsonObject.getJsonArray("unReadConversations")?.let {
            val listType = object : TypeToken<List<String>>() {}.type
            userAvailability.userIds = gson.fromJson(it, listType)
        }
        userAvailability.status = jsonObject.getString("status") ?: ""


        return userAvailability
    }
}