package com.rapidops.salesmatechatsdk.data.repositories

import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.reqmodels.SendMessageReq
import com.rapidops.salesmatechatsdk.data.resmodels.*
import com.rapidops.salesmatechatsdk.data.webserivce.IService
import com.rapidops.salesmatechatsdk.data.webserivce.MultipartUtil
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import com.rapidops.salesmatechatsdk.domain.exception.APIResponseMapper
import java.io.File

internal class ConversationRepository(private val service: IService) : IConversationDataSource {

    override suspend fun getConversationList(rows: Int, offSet: Int): ConversationRes {
        return APIResponseMapper.getResponse {
            service.getConversations(rows, offSet)
        }
    }

    override suspend fun getConversationsDetail(conversationId: String): ConversationDetailRes {
        return APIResponseMapper.getResponse {
            service.getConversations(conversationId, true)
        }
    }


    override suspend fun getMessageList(
        conversationId: String,
        rows: Int,
        offSet: Int,
        lastMessageDate: String?
    ): MessageListRes {
        return APIResponseMapper.getResponse {
            service.getMessages(conversationId, rows, offSet, lastMessageDate)
        }
    }

    override suspend fun sendMessages(
        conversationId: String,
        sendMessageReq: SendMessageReq
    ): SendMessageRes {
        return APIResponseMapper.getResponse {
            service.sendMessage(conversationId, sendMessageReq)
        }
    }

    override suspend fun readConversationForVisitor(conversationId: String): ConversationDetailRes {
        val body = hashMapOf<String, String>()
        body["conversation_id"] = conversationId
        return APIResponseMapper.getResponse {
            service.readConversationForVisitor(body)
        }
    }

    override suspend fun uploadFile(file: File): UploadFileRes {
        val filePart = MultipartUtil.prepareFilePart("file", file)
        return APIResponseMapper.getResponse { service.uploadFile(true, filePart) }
    }


    override suspend fun rating(conversationId: String, rating: String) {
        val body = hashMapOf<String, String>()
        body["rating"] = rating
        return APIResponseMapper.getResponse {
            service.rating(conversationId, body)
        }
    }

    override suspend fun remark(conversationId: String, remark: String) {
        val body = hashMapOf<String, String>()
        body["remark"] = remark
        return APIResponseMapper.getResponse {
            service.remark(conversationId, body)
        }
    }

}