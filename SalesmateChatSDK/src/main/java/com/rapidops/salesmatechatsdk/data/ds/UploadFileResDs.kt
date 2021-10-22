package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.resmodels.UploadFileRes
import com.rapidops.salesmatechatsdk.data.utils.getJsonObject
import com.rapidops.salesmatechatsdk.data.utils.getString
import java.lang.reflect.Type

internal class UploadFileResDs : JsonDeserializer<UploadFileRes> {
    /*{"Status":"success","Data":{"path":"c7dbde70-3323-11ec-99f0-d5648c577110",
   "fileName":"Screenshot 2021-10-16 140141.png","contentType":"image/png",
   "url":"https://files-dev.salesmate.io/generic/tmp/c7dbde70-3323-11ec-99f0-d5648c577110",
   "thumbnailPath":"c7dbde70-3323-11ec-99f0-d5648c577110-thumb",
   "thumbnailUrl":"https://files-dev.salesmate.io/generic/tmp/c7dbde70-3323-11ec-99f0-d5648c577110-thumb"}}*/

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): UploadFileRes {

        val uploadFileRes = UploadFileRes()

        val jsonObject = json.asJsonObject

        jsonObject.getJsonObject("Data")?.let { dataObject ->
            uploadFileRes.path = dataObject.getString("path") ?: ""
            uploadFileRes.fileName = dataObject.getString("fileName") ?: ""
            uploadFileRes.contentType = dataObject.getString("contentType") ?: ""
            uploadFileRes.url = dataObject.getString("url") ?: ""
            uploadFileRes.thumbnailPath = dataObject.getString("thumbnailPath")
            uploadFileRes.thumbnailUrl = dataObject.getString("thumbnailUrl")

        }
        return uploadFileRes
    }
}