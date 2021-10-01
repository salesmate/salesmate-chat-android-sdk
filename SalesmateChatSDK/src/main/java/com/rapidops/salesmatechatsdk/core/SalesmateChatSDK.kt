package com.rapidops.salesmatechatsdk.core

import android.app.Application

abstract class SalesmateChatSDK {

    companion object {

        private lateinit var instance: SalesmateChatSDK

        fun initialize(application: Application) {
            if (Companion::instance.isInitialized.not()) {
                synchronized(SalesmateChatSDK::javaClass) {
                    instance = SalesmateChat.create(application)
                }
            }
        }

        fun getInstance(): SalesmateChatSDK {
            if (Companion::instance.isInitialized) {
                return instance
            } else {
                throw IllegalStateException("Please call SalesMateChatSDK.initialize() before requesting the instance.")
            }
        }
    }

    abstract fun logDebug(msg: String)

    abstract fun startMessenger()

}