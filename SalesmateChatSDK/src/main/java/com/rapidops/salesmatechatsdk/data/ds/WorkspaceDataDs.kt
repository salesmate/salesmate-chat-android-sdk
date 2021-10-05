package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.WorkspaceData
import java.lang.reflect.Type

internal class WorkspaceDataDs : JsonDeserializer<WorkspaceData> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): WorkspaceData {

        val workspaceData = WorkspaceData()

        val jsonObject = json.asJsonObject

        workspaceData.id = jsonObject.getString("id") ?: ""
        workspaceData.linkname = jsonObject.getString("linkname") ?: ""
        workspaceData.name = jsonObject.getString("name") ?: ""
        workspaceData.description = jsonObject.getString("description") ?: ""

        return workspaceData
    }
}