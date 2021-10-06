package com.rapidops.salesmatechatsdk.domain.datasources

import com.rapidops.salesmatechatsdk.data.resmodels.ConversationRes
import com.rapidops.salesmatechatsdk.data.resmodels.GenerateTokenRes
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes

internal interface IAuthDataSource {

    suspend fun ping(tenantId: String): PingRes
    suspend fun generateToken(accessToken: String, pseudoName: String): GenerateTokenRes
    suspend fun getConversations(rows: Int, offSet: Int): ConversationRes
}