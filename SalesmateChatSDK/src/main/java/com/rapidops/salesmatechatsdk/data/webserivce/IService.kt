package com.rapidops.salesmatechatsdk.data.webserivce

import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.reqmodels.SendMessageReq
import com.rapidops.salesmatechatsdk.data.resmodels.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

internal interface IService {

    @GET("/data")
    suspend fun getData(): Response<JsonElement>


    @POST("v1/widget/ping")
    suspend fun ping(@Body body: Map<String, String>): Response<PingRes>

    @POST("v1/widget/generate-token")
    suspend fun generateToken(@Body body: Map<String, String>): Response<GenerateTokenRes>

    @GET("v1/widget/conversations")
    suspend fun getConversations(
        @Query("rows") rows: Int,
        @Query("offset") offset: Int
    ): Response<ConversationRes>

    @GET("v1/widget/conversations/{conversationId}")
    suspend fun getConversations(
        @Path("conversationId") conversationId: String,
        @Query("messages") messages: Boolean
    ): Response<ConversationDetailRes>

    @GET("v1/widget/conversations/{conversationId}/messages")
    suspend fun getMessages(
        @Path("conversationId") conversationId: String,
        @Query("rows") rows: Int,
        @Query("offset") offset: Int,
        @Query("lastMessageDate") lastMessageDate: String?
    ): Response<MessageListRes>


    @POST("v1/conversations/{conversationId}/message")
    suspend fun sendMessage(
        @Path("conversationId") conversationId: String?,
        @Body sendMessageReq: SendMessageReq
    ): Response<SendMessageRes>

    @POST("v1/widget/read-conversation-for-visitor")
    suspend fun readConversationForVisitor(
        @Body body: Map<String, String>
    ): Response<ConversationDetailRes>

    @Multipart
    @POST("v1/upload-file")
    suspend fun uploadFile(
        @Query("make_public") makePublic: Boolean,
        @Part bodyPart: MultipartBody.Part
    ): Response<UploadFileRes>

}
