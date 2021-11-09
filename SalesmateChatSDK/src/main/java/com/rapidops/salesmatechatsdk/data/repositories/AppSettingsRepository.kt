package com.rapidops.salesmatechatsdk.data.repositories

import android.content.Context
import android.content.ContextWrapper
import com.google.gson.Gson
import com.rapidops.salesmatechatsdk.core.SalesmateChatSettings
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.Prefs
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.Channel
import com.rapidops.salesmatechatsdk.domain.models.ContactData

internal class AppSettingsRepository(context: Context) : IAppSettingsDataSource {

    companion object {
        private const val PREF_NAME = "salesmatechatsdk_preferences"

        private const val PREF_ACCESS_TOKEN = "PREF_ACCESS_TOKEN"
        private const val PREF_PSEUDO_NAME = "PREF_PSEUDO_NAME"
        private const val PREF_CONTACT_DATA = "PREF_CONTACT_DATA"
        private const val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"
    }

    private var mContext: Context = context
    private var _salesMateChatSettings: SalesmateChatSettings = SalesmateChatSettings("", "", "")

    private var _verifiedId: String = ""
    private var _pingRes: PingRes = PingRes()
    private var _channel: Channel = Channel()

    private var gson: Gson = GsonUtils.gson
    init {
        Prefs.initPreferences(this.mContext, PREF_NAME, ContextWrapper.MODE_PRIVATE)
    }

    override var salesMateChatSetting: SalesmateChatSettings
        get() = _salesMateChatSettings
        set(value) {
            _salesMateChatSettings = value
        }

    override var verifiedId: String
        get() = _verifiedId
        set(value) {
            _verifiedId = value
        }


    override var androidUniqueId: String
        get() {
            return Prefs.getString(PREF_UNIQUE_ID, "") ?: ""
//            return "475849d4-e8ba-48c1-b719-2aa707f91b0b"
        }
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

    override val contactName: String
        get() = contactData?.name ?: pseudoName

    override val email: String?
        get() = contactData?.email

    override var contactData: ContactData?
        get() {
            val string = Prefs.getString(PREF_CONTACT_DATA, "")
            return if (string.isNullOrEmpty().not()) {
                gson.fromJson(string, ContactData::class.java)
            } else {
                null
            }
        }
        set(value) {
            Prefs.putString(PREF_CONTACT_DATA, gson.toJson(value))
        }

    override fun saveContactDetail(contactId: String, email: String, name: String) {
        contactData = ContactData(id = contactId, name = name, email = email)
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

    override val isContact: Boolean
        get() = contactData != null


    override val preventRepliesToCloseConversations: Boolean
        get() {
            return if (isContact) {
                pingRes.conversationsSettings?.preventRepliesToCloseConversationsForContacts == true
            } else {
                pingRes.conversationsSettings?.preventRepliesToCloseConversationsForVisitors == true
            }
        }

    override val preventRepliesToCloseConversationsWithinNumberOfDays: Int
        get() {
            return if (isContact) {
                pingRes.conversationsSettings?.preventRepliesToCloseConversationsWithinNumberOfDaysForContacts?.toInt()
                    ?: 0
            } else {
                pingRes.conversationsSettings?.preventRepliesToCloseConversationsWithinNumberOfDaysForVisitors?.toInt()
                    ?: 0
            }
        }

}
