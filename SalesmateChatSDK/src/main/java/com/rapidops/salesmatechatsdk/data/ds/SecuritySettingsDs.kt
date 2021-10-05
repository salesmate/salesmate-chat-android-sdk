package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getBoolean
import com.rapidops.salesmatechatsdk.domain.models.SecuritySettings
import java.lang.reflect.Type

internal class SecuritySettingsDs : JsonDeserializer<SecuritySettings> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): SecuritySettings {

        val securitySettings = SecuritySettings()

        val jsonObject = json.asJsonObject

        securitySettings.canUploadAttachment = jsonObject.getBoolean("can_upload_attachment")
        securitySettings.trustedDomains = jsonObject.getBoolean("trusted_domains")

        return securitySettings
    }
}