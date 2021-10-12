package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getBoolean
import com.rapidops.salesmatechatsdk.data.utils.getJsonObject
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.ContactData
import com.rapidops.salesmatechatsdk.domain.models.Owner
import java.lang.reflect.Type

internal class ContactDataDs : JsonDeserializer<ContactData> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ContactData {
        val gson = GsonUtils.gson

        val contactData = ContactData()

        val jsonObject = json.asJsonObject

        contactData.id = jsonObject.getString("id") ?: ""
        contactData.name = jsonObject.getString("name") ?: ""
        contactData.email = jsonObject.getString("email") ?: ""
        contactData.isDeleted = jsonObject.getBoolean("isDeleted")
        jsonObject.getJsonObject("owner")?.let {
            contactData.owner = gson.fromJson(it, Owner::class.java)
        }

        return contactData
    }
}