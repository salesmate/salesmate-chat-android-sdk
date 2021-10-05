package com.rapidops.salesmatechatsdk.domain.datasources

import com.google.gson.JsonElement

internal interface INameDataSource {

    suspend fun getData(): JsonElement
}