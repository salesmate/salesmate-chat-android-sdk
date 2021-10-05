package com.rapidops.salesmatechatsdk.core

import android.app.Application
import android.content.Intent
import android.util.Log
import com.facebook.stetho.Stetho
import com.rapidops.salesmatechatsdk.BuildConfig
import com.rapidops.salesmatechatsdk.app.activity.main.MainActivity
import com.rapidops.salesmatechatsdk.app.di.*
import java.util.*

internal class SalesmateChat(
    private val application: Application,
    private val salesMateChatSettings: SalesmateChatSettings
) : SalesmateChatSDK() {

    companion object {
        lateinit var daggerDataComponent: DataComponent

        private val TAG: String = SalesmateChat::class.java.simpleName

        fun create(
            application: Application,
            salesMateChatSettings: SalesmateChatSettings
        ): SalesmateChatSDK {
            return SalesmateChat(application, salesMateChatSettings)
        }
    }

    init {
        onCreate()
    }


    private fun onCreate() {
        daggerDataComponent = DaggerDataComponent.builder()
            .contextModule(ContextModule(application))
            .dataModule(DataModule())
            .networkModule(NetworkModule(BuildConfig.BASE_URL, BuildConfig.DEBUG))
            .viewModelModule(ViewModelModule())
            .build()

        val appSettingsDataSource = daggerDataComponent.getAppSettingsDataSource()
        appSettingsDataSource.salesMateChatSetting = salesMateChatSettings
        if (appSettingsDataSource.androidUniqueId.isEmpty()) {
            appSettingsDataSource.androidUniqueId = UUID.randomUUID().toString()
        }
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