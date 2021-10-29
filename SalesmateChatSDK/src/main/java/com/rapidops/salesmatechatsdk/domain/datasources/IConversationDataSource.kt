package com.rapidops.salesmatechatsdk.domain.datasources

import com.rapidops.salesmatechatsdk.data.reqmodels.SendMessageReq
import com.rapidops.salesmatechatsdk.data.resmodels.*
import java.io.File

internal interface IConversationDataSource {

    suspend fun getConversationList(rows: Int, offSet: Int): ConversationRes
    suspend fun getConversationsDetail(conversationId: String): ConversationDetailRes
    suspend fun getMessageList(
        conversationId: String,
        rows: Int,
        offSet: Int,
        lastMessageDate: String?
    ): MessageListRes

    suspend fun sendMessages(conversationId: String, sendMessageReq: SendMessageReq): SendMessageRes
    suspend fun readConversationForVisitor(conversationId: String): ConversationDetailRes
    suspend fun uploadFile(file: File): UploadFileRes
    suspend fun rating(conversationId: String, rating: String)
    suspend fun remark(conversationId: String, remark: String)
    suspend fun contact(conversationId: String?, email: String)
    suspend fun track(body: Map<String, String>)
    suspend fun downloadTranscript(conversationId: String): DownloadTranscriptRes
}