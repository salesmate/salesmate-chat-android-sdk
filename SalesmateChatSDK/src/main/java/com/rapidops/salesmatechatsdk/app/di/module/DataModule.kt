package com.rapidops.salesmatechatsdk.app.di.module

import android.content.Context
import com.rapidops.salesmatechatsdk.app.di.ApplicationScope
import com.rapidops.salesmatechatsdk.data.repositories.*
import com.rapidops.salesmatechatsdk.data.webserivce.IService
import com.rapidops.salesmatechatsdk.domain.datasources.*
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

    @Provides
    @ApplicationScope
    internal fun provideConversationRepository(service: IService): IConversationDataSource {
        return ConversationRepository(service)
    }

    @Provides
    @ApplicationScope
    internal fun provideAnalyticsRepository(
        context: Context,
        appSettingsDataSource: IAppSettingsDataSource,
        service: IService
    ): IAnalyticsDataSource {
        return AnalyticsRepository(context, appSettingsDataSource, service)
    }

}
