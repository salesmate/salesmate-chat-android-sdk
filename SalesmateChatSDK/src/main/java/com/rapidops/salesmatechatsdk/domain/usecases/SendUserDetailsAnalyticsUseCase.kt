package com.rapidops.salesmatechatsdk.domain.usecases

import com.google.gson.JsonObject
import com.rapidops.salesmatechatsdk.domain.datasources.IAnalyticsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject


internal class SendUserDetailsAnalyticsUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val analyticsDataSource: IAnalyticsDataSource,
) :
    UseCase<SendUserDetailsAnalyticsUseCase.Param, Boolean>() {


    override suspend fun execute(params: Param?): Boolean {
        val trackEventParams = params!!

        val body = hashMapOf<String, String>()

        val userDetailJson = JsonObject()/*.apply {
            addProperty("email", trackEventParams.email)
            addProperty("name", trackEventParams.name)
        }.toString()*/

        trackEventParams.userDetailMap.forEach {
            userDetailJson.addProperty(it.key, it.value)
            body[it.key] = it.value
        }

        body["user_details"] = userDetailJson.toString()

        body["app_key"] = appSettingsDataSource.salesMateChatSetting.appKey
        body["device_id"] = appSettingsDataSource.androidUniqueId
        body["sdk_name"] = "sm-analytics"
        body["uuid"] = UUID.randomUUID().toString()
        body["tenant_id"] = appSettingsDataSource.salesMateChatSetting.tenantId
        /*body["email"] = userDetailJson.get("email").asString ?: ""
        body["name"] = userDetailJson.get("name").asString ?: ""*/
        body["visitor_id"] = appSettingsDataSource.androidUniqueId
        body["session_id"] = appSettingsDataSource.androidUniqueId
        body["timestamp"] = Date().time.toString()
        body["hour"] = DateTime.now().hourOfDay.toString()

        analyticsDataSource.sendUserDetails(body)
        return true
    }

    data class Param(val userDetailMap: Map<String, String>)

}

