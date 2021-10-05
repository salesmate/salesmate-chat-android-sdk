package com.rapidops.salesmatechatsdk.data.repositories

import com.rapidops.salesmatechatsdk.data.resmodels.GenerateTokenRes
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.data.webserivce.IService
import com.rapidops.salesmatechatsdk.domain.datasources.IAuthDataSource
import com.rapidops.salesmatechatsdk.domain.exception.APIResponseMapper

internal class AuthRepository(private val service: IService) : IAuthDataSource {

    override suspend fun ping(tenantId: String): PingRes {
        val bodyMap = hashMapOf<String, String>()
        bodyMap["referer"] = tenantId
        return APIResponseMapper.getResponse {
            service.ping(bodyMap)
        }
    }


    override suspend fun generateToken(accessToken: String, pseudoName: String): GenerateTokenRes {
        val bodyMap = hashMapOf<String, String>()
        bodyMap["accessToken"] = accessToken
        bodyMap["pseudo_name"] = pseudoName
        return APIResponseMapper.getResponse {
            service.generateToken(bodyMap)
        }
    }
}