package com.rapidops.salesmatechatsdk.data.repositories

import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.webserivce.IService
import com.rapidops.salesmatechatsdk.domain.datasources.INameDataSource
import com.rapidops.salesmatechatsdk.domain.exception.APIResponseMapper

internal class NameRepository(private val service: IService) : INameDataSource {

    override suspend fun getData(): JsonElement {
        return APIResponseMapper.getResponse {
            service.getData()
        }
    }
}