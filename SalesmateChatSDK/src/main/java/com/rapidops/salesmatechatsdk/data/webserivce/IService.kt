package com.rapidops.salesmatechatsdk.data.webserivce

import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.resmodels.GenerateTokenRes
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

internal interface IService {

    @GET("/data")
    suspend fun getData(): Response<JsonElement>


    @POST("v1/widget/ping")
    suspend fun ping(@Body body: Map<String, String>): Response<PingRes>

    @POST("v1/widget/generate-token")
    suspend fun generateToken(@Body body: Map<String, String>): Response<GenerateTokenRes>

}
