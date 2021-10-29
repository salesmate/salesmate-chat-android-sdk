package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.resmodels.DownloadTranscriptRes
import com.rapidops.salesmatechatsdk.data.utils.getString
import java.lang.reflect.Type

internal class DownloadTranscriptResDs : JsonDeserializer<DownloadTranscriptRes> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): DownloadTranscriptRes {

        val downloadTranscriptRes = DownloadTranscriptRes()

        val jsonObject = json.asJsonObject

        downloadTranscriptRes.status = jsonObject.getString("Status") ?: ""
        downloadTranscriptRes.data = jsonObject.getString("Data") ?: ""

        return downloadTranscriptRes
    }
}