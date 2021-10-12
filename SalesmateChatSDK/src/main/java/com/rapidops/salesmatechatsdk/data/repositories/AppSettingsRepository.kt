package com.rapidops.salesmatechatsdk.data.repositories

import android.content.Context
import android.content.ContextWrapper
import com.rapidops.salesmatechatsdk.core.SalesmateChatSettings
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.data.utils.Prefs
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.Channel

internal class AppSettingsRepository(context: Context) : IAppSettingsDataSource {

    companion object {
        private const val PREF_NAME = "salesmatechatsdk_preferences"

        private const val PREF_ACCESS_TOKEN = "PREF_ACCESS_TOKEN"
        private const val PREF_PSEUDO_NAME = "PREF_PSEUDO_NAME"
        private const val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"
    }

    private var mContext: Context = context
    private var _salesMateChatSettings: SalesmateChatSettings = SalesmateChatSettings("", "", "")

    private var _pingRes: PingRes = PingRes()
    private var _channel: Channel = Channel()

    init {
        Prefs.initPreferences(this.mContext, PREF_NAME, ContextWrapper.MODE_PRIVATE)
    }

    override var salesMateChatSetting: SalesmateChatSettings
        get() = _salesMateChatSettings
        set(value) {
            _salesMateChatSettings = value
        }


    override var androidUniqueId: String
        get() = Prefs.getString(PREF_UNIQUE_ID, "") ?: ""
        set(value) {
            Prefs.putString(PREF_UNIQUE_ID, value)
        }

    override var accessToken: String
        get() = Prefs.getString(PREF_ACCESS_TOKEN, "") ?: ""
        set(value) {
            Prefs.putString(PREF_ACCESS_TOKEN, value)
        }

    override var pseudoName: String
        get() = Prefs.getString(PREF_PSEUDO_NAME, "") ?: ""
        set(value) {
            Prefs.putString(PREF_PSEUDO_NAME, value)
        }

    override var pingRes: PingRes
        get() = _pingRes
        set(value) {
            _pingRes = value
        }

    override val linkName: String
        get() = _pingRes.linkname


    override var channel: Channel
        get() = _channel
        set(value) {
            _channel = value
        }
}
