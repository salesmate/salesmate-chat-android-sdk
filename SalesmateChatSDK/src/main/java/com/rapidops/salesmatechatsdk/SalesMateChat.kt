package com.rapidops.salesmatechatsdk

import android.app.Application
import android.util.Log

internal class SalesMateChat : SalesMateChatSDK() {

    companion object {

        private val TAG: String = SalesMateChat::class.java.simpleName

        private lateinit var instance: SalesMateChat

        fun create(application: Application): SalesMateChat {
            if (::instance.isInitialized.not()) {
                synchronized(SalesMateChat::javaClass) {
                    instance = SalesMateChat()
                }
            }
            return instance
        }
    }

    override fun logDebug(msg: String) {
        Log.d(TAG, msg)
    }
}