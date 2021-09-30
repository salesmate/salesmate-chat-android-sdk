package com.rapidops.salesmatechatsdk

import android.app.Application

abstract class SalesMateChatSDK {

    companion object {

        private lateinit var instance: SalesMateChatSDK

        fun initialize(application: Application) {
            if (::instance.isInitialized.not()) {
                synchronized(SalesMateChatSDK::javaClass) {
                    instance = SalesMateChat.create(application)
                }
            }
        }

        fun getInstance(): SalesMateChatSDK {
            if (::instance.isInitialized) {
                return instance
            } else {
                throw IllegalStateException("Please call SalesMateChatSDK.initialize() before requesting the instance.")
            }
        }
    }

    abstract fun logDebug(msg: String)

}