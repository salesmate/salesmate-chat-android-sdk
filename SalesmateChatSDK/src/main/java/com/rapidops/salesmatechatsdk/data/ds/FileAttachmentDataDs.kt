package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.message.FileAttachmentData
import java.lang.reflect.Type

internal class FileAttachmentDataDs : JsonDeserializer<FileAttachmentData> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): FileAttachmentData {

        val fileAttachmentData = FileAttachmentData()

        val jsonObject = json.asJsonObject

        fileAttachmentData.workspaceId = jsonObject.getString("workspace_id") ?: ""
        fileAttachmentData.gcpFileName = jsonObject.getString("gcp_file_name") ?: ""
        fileAttachmentData.thumbnail = jsonObject.getString("thumbnail") ?: ""
        fileAttachmentData.contentType = jsonObject.getString("content_type") ?: ""
        fileAttachmentData.size = jsonObject.getString("size") ?: ""
        fileAttachmentData.conversationId = jsonObject.getString("conversation_id") ?: ""
        fileAttachmentData.name = jsonObject.getString("name") ?: ""
        fileAttachmentData.linkUrl = jsonObject.getString("link_url") ?: ""
        fileAttachmentData.id = jsonObject.getString("id") ?: ""
        fileAttachmentData.linkname = jsonObject.getString("linkname") ?: ""
        fileAttachmentData.url = jsonObject.getString("url") ?: ""

        return fileAttachmentData
    }
}