package com.rapidops.salesmatechatsdk.app.di.component

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.app.di.ApplicationScope
import com.rapidops.salesmatechatsdk.app.di.module.ContextModule
import com.rapidops.salesmatechatsdk.app.di.module.DataModule
import com.rapidops.salesmatechatsdk.app.di.module.NetworkModule
import com.rapidops.salesmatechatsdk.app.di.module.ViewModelModule
import com.rapidops.salesmatechatsdk.app.socket.SocketController
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.usecases.GenerateTokenUseCase
import com.rapidops.salesmatechatsdk.domain.usecases.SendAnalyticsUseCase
import com.rapidops.salesmatechatsdk.domain.usecases.SendUserDetailsAnalyticsUseCase
import dagger.Component

@Component(modules = [ContextModule::class, DataModule::class, NetworkModule::class, ViewModelModule::class])
@ApplicationScope
internal interface DataComponent {

    fun getViewModelFactory(): ViewModelProvider.Factory
    fun getCoroutineContextProvider(): ICoroutineContextProvider
    fun getAppSettingsDataSource(): IAppSettingsDataSource
    fun getContext(): Context
    fun getSocketController(): SocketController
    fun getSendAnalyticsUseCase(): SendAnalyticsUseCase
    fun getSendUserDetailsAnalyticsUseCase(): SendUserDetailsAnalyticsUseCase
    fun getGenerateTokenUseCase(): GenerateTokenUseCase

}
