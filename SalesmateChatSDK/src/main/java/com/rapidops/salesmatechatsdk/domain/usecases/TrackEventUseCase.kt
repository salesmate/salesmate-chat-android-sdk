package com.rapidops.salesmatechatsdk.domain.usecases

import com.google.gson.JsonObject
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject


internal class TrackEventUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val conversationDataSource: IConversationDataSource,
) :
    UseCase<TrackEventUseCase.Param, Boolean>() {


    override suspend fun execute(params: Param?): Boolean {
        val trackEventParams = params!!

        val body = hashMapOf<String, String>()

        val userDetailJson = JsonObject().apply {
            addProperty("email", trackEventParams.email)
            addProperty("name", trackEventParams.name)
        }.toString()

        body["user_details"] = userDetailJson
        body["app_key"] = appSettingsDataSource.salesMateChatSetting.appKey
        body["device_id"] = appSettingsDataSource.androidUniqueId
        body["sdk_name"] = "sm-analytics"
        body["uuid"] = UUID.randomUUID().toString()
        body["tenant_id"] = appSettingsDataSource.salesMateChatSetting.tenantId
        body["email"] = trackEventParams.email
        body["name"] = trackEventParams.name
        body["visitor_id"] = UUID.randomUUID().toString()
        body["session_id"] = trackEventParams.sessionId
        body["timestamp"] = Date().time.toString()
        body["hour"] = DateTime.now().hourOfDay.toString()

        conversationDataSource.track(body)
        return true
    }

    data class Param(val name: String, val email: String, val sessionId: String)

}

