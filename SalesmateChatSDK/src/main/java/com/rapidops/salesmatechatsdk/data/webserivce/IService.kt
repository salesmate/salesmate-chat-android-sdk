package com.rapidops.salesmatechatsdk.data.webserivce

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.GET

interface IService {

    @GET("/data")
    suspend fun getData(): Response<JsonElement>

}
