package com.rapidops.salesmatechat.core

import android.app.Application
import com.rapidops.salesmatechatsdk.core.SalesmateChatSDK
import com.rapidops.salesmatechatsdk.core.SalesmateChatSettings
import com.rapidops.salesmatechatsdk.domain.models.BuildType

class SalesmateChatApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val workspaceId = "15b160f2-7fcf-4295-9236-81d120c5b47c"
        val appKey = "dac84910-21bc-11ec-adce-354c2694d0d3"
        val tenantId = "dev27.salesmate.io"
        val salesmateChatSettings =
            SalesmateChatSettings(workspaceId, appKey, tenantId, BuildType.DEVELOPMENT)

        SalesmateChatSDK.initialize(this, salesmateChatSettings)
    }
}