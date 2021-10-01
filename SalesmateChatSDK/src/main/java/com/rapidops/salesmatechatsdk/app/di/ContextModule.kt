package com.rapidops.salesmatechatsdk.app.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule(private val context: Context) {

    @Provides
    @ApplicationScope
    internal fun provideApplicationContext(): Context {
        return context
    }

}
