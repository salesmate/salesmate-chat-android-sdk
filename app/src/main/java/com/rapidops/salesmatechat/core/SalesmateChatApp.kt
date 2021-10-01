package com.rapidops.salesmatechat.core

import android.app.Application
import com.rapidops.salesmatechatsdk.core.SalesmateChatSDK

class SalesmateChatApp : Application() {
    override fun onCreate() {
        super.onCreate()

        SalesmateChatSDK.initialize(this)
    }
}