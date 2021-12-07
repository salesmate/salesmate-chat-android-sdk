package com.rapidops.salesmatechatsdk.domain.datasources

internal interface IAnalyticsDataSource {

    suspend fun sendEvent(eventName: String, extraPayLoad: HashMap<String, String>)

    suspend fun sendUserDetails(body: Map<String, String>)
}