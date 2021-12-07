package com.rapidops.salesmatechatsdk.data.repositories

import android.content.Context
import com.rapidops.salesmatechatsdk.BuildConfig
import com.rapidops.salesmatechatsdk.data.webserivce.IService
import com.rapidops.salesmatechatsdk.domain.datasources.IAnalyticsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.exception.APIResponseMapper
import com.rapidops.sdk.ly.rapidops.android.sdk.Rapidops

internal class AnalyticsRepository(
    context: Context,
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val service: IService
) : IAnalyticsDataSource {
    private var context: Context

    init {

        this.context = context
        try {
            Rapidops.sharedInstance().isLoggingEnabled = true
            Rapidops.sharedInstance().isHttpPostForced = true
            Rapidops.sharedInstance().setViewTracking(true)
            Rapidops.sharedInstance().setAutoTrackingUseShortName(true)

            val customHeaderValues = java.util.HashMap<String, String>()
            //customHeaderValues["Content-Type"] = "application/json"
            customHeaderValues["x-linkname"] = appSettingsDataSource.salesMateChatSetting.tenantId
            Rapidops.sharedInstance().addCustomNetworkRequestHeaders(customHeaderValues)

            Rapidops.sharedInstance().setTenantID(appSettingsDataSource.salesMateChatSetting.tenantId)
            Rapidops.sharedInstance().init(
                context,
                BuildConfig.TRACK_API_URL,
                "123",
                appSettingsDataSource.androidUniqueId
            )
        } catch (e: Exception) {

        }
    }

    override suspend fun sendEvent(eventName: String, extraPayLoad: HashMap<String, String>) {
        try {
            Rapidops.sharedInstance().recordEvent(eventName, extraPayLoad, 1)
        } catch (e: Exception) {

        }
    }

    override suspend fun sendUserDetails(body: Map<String, String>) {
        val url = BuildConfig.TRACK_API_URL + "/track"
        return APIResponseMapper.getResponse {
            service.track(url, body)
        }
    }
}