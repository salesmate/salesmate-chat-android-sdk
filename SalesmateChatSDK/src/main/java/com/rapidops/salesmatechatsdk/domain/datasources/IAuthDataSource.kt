package com.rapidops.salesmatechatsdk.domain.datasources

import com.rapidops.salesmatechatsdk.data.resmodels.GenerateTokenRes
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes

internal interface IAuthDataSource {

    suspend fun ping(tenantId: String): PingRes
    suspend fun generateToken(accessToken: String, pseudoName: String): GenerateTokenRes
}