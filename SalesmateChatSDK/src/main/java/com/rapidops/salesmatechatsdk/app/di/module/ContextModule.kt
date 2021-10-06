package com.rapidops.salesmatechatsdk.app.di.module

import android.content.Context
import com.rapidops.salesmatechatsdk.app.di.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
internal class ContextModule(private val context: Context) {

    @Provides
    @ApplicationScope
    internal fun provideApplicationContext(): Context {
        return context
    }

}
