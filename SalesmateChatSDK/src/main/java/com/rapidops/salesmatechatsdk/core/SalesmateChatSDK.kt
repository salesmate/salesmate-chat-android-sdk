package com.rapidops.salesmatechatsdk.core

import android.app.Application

abstract class SalesmateChatSDK {

    companion object {

        private lateinit var instance: SalesmateChatSDK

        fun initialize(application: Application, salesmateChatSettings: SalesmateChatSettings) {
            if (isSettingsValid(salesmateChatSettings)) {
                if (Companion::instance.isInitialized.not()) {
                    synchronized(SalesmateChatSDK::javaClass) {
                        instance = SalesmateChat.create(application, salesmateChatSettings)
                    }
                }
            } else {
                throw IllegalStateException("Please provide valid SalesmateChatSetting value for initialize.")
            }
        }

        private fun isSettingsValid(salesmateChatSettings: SalesmateChatSettings): Boolean {
            return salesmateChatSettings.appKey.isNotEmpty() &&
                    salesmateChatSettings.tenantId.isNotEmpty() &&
                    salesmateChatSettings.workspaceId.isNotEmpty()
        }

        fun getInstance(): SalesmateChatSDK {
            if (Companion::instance.isInitialized) {
                return instance
            } else {
                throw IllegalStateException("Please call SalesmateChatSDK.initialize() before requesting the instance.")
            }
        }
    }

    abstract fun logDebug(msg: String)

    abstract fun startMessenger()

    abstract fun recordEvent(eventName: String, data: HashMap<String, String>)

    abstract fun setVerifiedId(verifiedId: String)

}