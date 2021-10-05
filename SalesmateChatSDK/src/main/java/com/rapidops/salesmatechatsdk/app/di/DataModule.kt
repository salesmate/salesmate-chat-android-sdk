package com.rapidops.salesmatechatsdk.app.di

import android.content.Context
import com.rapidops.salesmatechatsdk.data.repositories.AppSettingsRepository
import com.rapidops.salesmatechatsdk.data.repositories.AuthRepository
import com.rapidops.salesmatechatsdk.data.repositories.NameRepository
import com.rapidops.salesmatechatsdk.data.webserivce.IService
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IAuthDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.INameDataSource
import dagger.Module
import dagger.Provides

@Module
internal class DataModule {

    @Provides
    @ApplicationScope
    internal fun provideAppSettings(applicationContext: Context): IAppSettingsDataSource {
        return AppSettingsRepository(applicationContext)
    }

    @Provides
    @ApplicationScope
    internal fun provideName(service: IService): INameDataSource {
        return NameRepository(service)
    }

    @Provides
    @ApplicationScope
    internal fun provideAuthRepository(service: IService): IAuthDataSource {
        return AuthRepository(service)
    }

}
