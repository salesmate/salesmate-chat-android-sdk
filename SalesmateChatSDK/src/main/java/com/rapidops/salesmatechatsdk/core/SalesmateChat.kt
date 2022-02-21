package com.rapidops.salesmatechatsdk.core

import android.app.Application
import android.content.Intent
import android.util.Log
import com.facebook.stetho.Stetho
import com.rapidops.salesmatechatsdk.BuildConfig
import com.rapidops.salesmatechatsdk.app.activity.main.MainActivity
import com.rapidops.salesmatechatsdk.app.di.component.DaggerDataComponent
import com.rapidops.salesmatechatsdk.app.di.component.DataComponent
import com.rapidops.salesmatechatsdk.app.di.module.ContextModule
import com.rapidops.salesmatechatsdk.app.di.module.DataModule
import com.rapidops.salesmatechatsdk.app.di.module.NetworkModule
import com.rapidops.salesmatechatsdk.app.di.module.ViewModelModule
import com.rapidops.salesmatechatsdk.app.interfaces.LoginListener
import com.rapidops.salesmatechatsdk.app.interfaces.UpdateListener
import com.rapidops.salesmatechatsdk.app.socket.SocketController
import com.rapidops.salesmatechatsdk.app.utils.NetworkUtil
import com.rapidops.salesmatechatsdk.domain.exception.SalesmateChatException
import com.rapidops.salesmatechatsdk.domain.exception.SalesmateException
import com.rapidops.salesmatechatsdk.domain.usecases.GenerateTokenUseCase
import com.rapidops.salesmatechatsdk.domain.usecases.SendAnalyticsUseCase
import com.rapidops.salesmatechatsdk.domain.usecases.SendUserDetailsAnalyticsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private lateinit var sendAnalyticsUseCase: SendAnalyticsUseCase
    private lateinit var sendUserDetailsAnalyticsUseCase: SendUserDetailsAnalyticsUseCase
    private lateinit var generateTokenUseCase: GenerateTokenUseCase
    private lateinit var socketController: SocketController

    private fun onCreate() {
        daggerDataComponent = DaggerDataComponent.builder()
            .contextModule(ContextModule(application))
            .dataModule(DataModule())
            .networkModule(NetworkModule(BuildConfig.BASE_API_URL, BuildConfig.DEBUG))
            .viewModelModule(ViewModelModule())
            .build()

        val appSettingsDataSource = daggerDataComponent.getAppSettingsDataSource()
        appSettingsDataSource.salesMateChatSetting = salesMateChatSettings
        if (appSettingsDataSource.androidUniqueId.isEmpty()) {
            appSettingsDataSource.androidUniqueId = UUID.randomUUID().toString()
        }
        sendAnalyticsUseCase = daggerDataComponent.getSendAnalyticsUseCase()
        sendUserDetailsAnalyticsUseCase = daggerDataComponent.getSendUserDetailsAnalyticsUseCase()
        generateTokenUseCase = daggerDataComponent.getGenerateTokenUseCase()
        socketController = daggerDataComponent.getSocketController()
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

    override fun recordEvent(eventName: String, data: HashMap<String, String>) {
        GlobalScope.launch {
            val param = SendAnalyticsUseCase.Param(eventName, data)
            sendAnalyticsUseCase.execute(param)
        }
    }

    private fun initDebuggers() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(application)
        }
    }

    override fun login(userId: String, userDetails: UserDetails, loginListener: LoginListener?) {
        if (NetworkUtil.isNetworkAvailable(application)) {
            if (userId.isNotBlank()) {
                GlobalScope.launch {
                    try {
                        val userDetailMap = hashMapOf<String, String>()
                        userDetailMap["first_name"] = userDetails.getFirstName()
                        userDetailMap["last_name"] = userDetails.getLastName()
                        userDetailMap["email"] = userDetails.getEmail()
                        userDetailMap["user_id"] = userId
                        val param = SendUserDetailsAnalyticsUseCase.Param(userDetailMap)
                        sendUserDetailsAnalyticsUseCase.execute(param)
                        daggerDataComponent.getAppSettingsDataSource().verifiedId = userId
                        generateTokenUseCase.execute()
                        socketController.resetSocketAndConnect()
                        withContext(Dispatchers.Main){
                            loginListener?.onLogin()
                        }
                    } catch (e: Exception) {
                        val salesmateException = getSalesMateException(e)
                        withContext(Dispatchers.Main){
                            loginListener?.onError(salesmateException)
                        }
                        e.printStackTrace()
                    }
                }
            } else {
                loginListener?.onError(SalesmateException.EmptyUserIdException)
            }
        } else {
            loginListener?.onError(SalesmateException.NoInternetException)
        }
    }

    override fun update(userId: String, userDetails: UserDetails, updateListener: UpdateListener?) {
        if (NetworkUtil.isNetworkAvailable(application)) {
            if (userId.isNotBlank()) {
                GlobalScope.launch {
                    try {
                        val userDetailMap = hashMapOf<String, String>()
                        if (userDetails.getFirstName().isNotEmpty()) {
                            userDetailMap["first_name"] = userDetails.getFirstName()
                        }
                        if (userDetails.getLastName().isNotEmpty()) {
                            userDetailMap["last_name"] = userDetails.getLastName()
                        }
                        if (userDetails.getEmail().isNotEmpty()) {
                            userDetailMap["email"] = userDetails.getEmail()
                        }
                        userDetailMap["user_id"] = userId
                        val param = SendUserDetailsAnalyticsUseCase.Param(userDetailMap)
                        sendUserDetailsAnalyticsUseCase.execute(param)
                        daggerDataComponent.getAppSettingsDataSource().verifiedId = userId
                        withContext(Dispatchers.Main){
                            updateListener?.onUpdate()
                        }
                    } catch (e: Exception) {
                        val salesmateException = getSalesMateException(e)
                        withContext(Dispatchers.Main){
                            updateListener?.onError(salesmateException)
                        }
                        e.printStackTrace()
                    }
                }
            } else {
                updateListener?.onError(SalesmateException.EmptyUserIdException)
            }
        } else {
            updateListener?.onError(SalesmateException.NoInternetException)
        }
    }

    override fun logout() {
        daggerDataComponent.getAppSettingsDataSource().clearLocalStorage()
        daggerDataComponent.getSocketController().resetSocket()
    }

    override fun getVisitorId(): String {
        return daggerDataComponent.getAppSettingsDataSource().verifiedId
    }

    private fun getSalesMateException(exception: Exception): SalesmateException {
        if (exception is SalesmateChatException) {
            when (exception.kind) {
                SalesmateChatException.Kind.UNEXPECTED, SalesmateChatException.Kind.NETWORK -> {
                    return SalesmateException.UnExpectedError
                }
                SalesmateChatException.Kind.REST_API -> {
                    exception.error?.let {
                        return SalesmateException(it.name, it.message)
                    }
                    return SalesmateException("", exception.message ?: "", exception)
                }
            }
        } else {
            return SalesmateException("", exception.message ?: "", exception)
        }
    }

}