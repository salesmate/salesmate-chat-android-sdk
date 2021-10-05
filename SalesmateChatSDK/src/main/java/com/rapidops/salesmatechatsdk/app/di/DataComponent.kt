package com.rapidops.salesmatechatsdk.app.di

import androidx.lifecycle.ViewModelProvider
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import dagger.Component

@Component(modules = [ContextModule::class, DataModule::class, NetworkModule::class,ViewModelModule::class])
@ApplicationScope
internal interface DataComponent {

    fun getViewModelFactory(): ViewModelProvider.Factory
    fun getCoroutineContextProvider(): ICoroutineContextProvider
    fun getAppSettingsDataSource(): IAppSettingsDataSource

}
