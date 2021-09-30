package com.rapidops.salesmatechat.core

import android.app.Application
import com.rapidops.salesmatechatsdk.SalesMateChatSDK

class SalesMateChatApp : Application() {
    override fun onCreate() {
        super.onCreate()

        SalesMateChatSDK.initialize(this)
    }
}