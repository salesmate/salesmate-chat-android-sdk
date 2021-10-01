package com.rapidops.salesmatechatsdk.core

import android.app.Application
import android.content.Intent
import android.util.Log
import com.facebook.stetho.Stetho
import com.rapidops.salesmatechatsdk.BuildConfig
import com.rapidops.salesmatechatsdk.app.activity.main.MainActivity
import com.rapidops.salesmatechatsdk.app.di.*

internal class SalesmateChat(private val application: Application) : SalesmateChatSDK() {

    companion object {
        lateinit var daggerDataComponent: DataComponent

        private val TAG: String = SalesmateChat::class.java.simpleName

        private lateinit var instance: SalesmateChat

        fun create(application: Application): SalesmateChatSDK {
            if (::instance.isInitialized.not()) {
                synchronized(SalesmateChat::javaClass) {
                    instance = SalesmateChat(application)
                }
            }
            return instance
        }
    }

    init {
        onCreate()
    }

    private fun onCreate() {
        daggerDataComponent = DaggerDataComponent.builder()
            .contextModule(ContextModule(application))
            .dataModule(DataModule())
            .networkModule(NetworkModule("https://google.com", BuildConfig.DEBUG))
            .viewModelModule(ViewModelModule())
            .build()

        initDebuggers()

    }

    override fun logDebug(msg: String) {
        Log.d(TAG, msg)
    }

    override fun startMessenger() {
        val context = application.applicationContext
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun initDebuggers(){
        Stetho.initializeWithDefaults(application)
    }
}