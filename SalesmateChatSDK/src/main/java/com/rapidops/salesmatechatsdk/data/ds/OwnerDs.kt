package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getBoolean
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.ContactData
import com.rapidops.salesmatechatsdk.domain.models.Owner
import java.lang.reflect.Type

internal class OwnerDs : JsonDeserializer<Owner> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Owner {

        val owner = Owner()

        val jsonObject = json.asJsonObject

        owner.id = jsonObject.getString("id") ?: ""
        owner.name = jsonObject.getString("name") ?: ""
        owner.firstName = jsonObject.getString("firstName") ?: ""
        owner.lastName = jsonObject.getString("lastName") ?: ""
        owner.photo = jsonObject.getString("photo") ?: ""
        owner.email = jsonObject.getString("email") ?: ""
        owner.mobile = jsonObject.getString("mobile") ?: ""


        return owner
    }
}