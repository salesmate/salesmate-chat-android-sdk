package com.rapidops.salesmatechatsdk.domain.datasources

import com.google.gson.JsonElement

interface INameDataSource {

    suspend fun getData(): JsonElement
}