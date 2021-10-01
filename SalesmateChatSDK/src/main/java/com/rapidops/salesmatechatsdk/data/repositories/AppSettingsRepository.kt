package com.rapidops.salesmatechatsdk.data.repositories

import android.content.Context
import android.content.ContextWrapper
import com.rapidops.salesmatechatsdk.data.utils.Prefs
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource

class AppSettingsRepository(context: Context) : IAppSettingsDataSource {

    companion object {
        private const val PREF_NAME = "salesmatechat_preferences"
    }

    private var mContext: Context = context

    init {
        Prefs.initPreferences(this.mContext, PREF_NAME, ContextWrapper.MODE_PRIVATE)
    }

}
