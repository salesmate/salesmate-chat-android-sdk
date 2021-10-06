package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getInt
import com.rapidops.salesmatechatsdk.data.utils.getJsonObject
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.Location
import com.rapidops.salesmatechatsdk.domain.models.User
import java.lang.reflect.Type

internal class UserDs : JsonDeserializer<User> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): User {

        val user = User()

        val jsonObject = json.asJsonObject

        user.id = jsonObject.getString("id") ?: ""
        user.firstName = jsonObject.getString("firstName") ?: ""
        user.lastName = jsonObject.getString("lastName") ?: ""
        user.profileId = jsonObject.getInt("profileId")
        user.roleId = jsonObject.getInt("roleId")
        user.profileUrl = jsonObject.getString("profileUrl") ?: ""


        jsonObject.getJsonObject("location")?.let {
            user.location = GsonUtils.gson.fromJson(it, Location::class.java)
        }

        user.linkname = jsonObject.getString("linkname") ?: ""
        user.availabilityMode = jsonObject.getString("availability_mode") ?: ""
        user.availabilityStatus = jsonObject.getString("availability_status") ?: ""
        user.lastSeenAt = jsonObject.getString("last_seen_at") ?: ""
        user.status = jsonObject.getString("status") ?: ""

        return user
    }
}