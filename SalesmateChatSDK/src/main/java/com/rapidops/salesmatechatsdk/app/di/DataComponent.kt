package com.rapidops.salesmatechatsdk.app.di

import androidx.lifecycle.ViewModelProvider
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import dagger.Component

@Component(modules = [ContextModule::class, DataModule::class, NetworkModule::class,ViewModelModule::class])
@ApplicationScope
interface DataComponent {

    fun getViewModelFactory(): ViewModelProvider.Factory
    fun getCoroutineContextProvider(): ICoroutineContextProvider

}
