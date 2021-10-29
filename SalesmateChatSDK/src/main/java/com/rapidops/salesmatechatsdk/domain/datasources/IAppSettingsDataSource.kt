package com.rapidops.salesmatechatsdk.domain.datasources

import com.rapidops.salesmatechatsdk.core.SalesmateChatSettings
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.domain.models.Channel
import com.rapidops.salesmatechatsdk.domain.models.ContactData

internal interface IAppSettingsDataSource {

    var salesMateChatSetting: SalesmateChatSettings
    var verifiedId: String

    var androidUniqueId: String

    var accessToken: String
    var pseudoName: String

    var pingRes: PingRes
    var channel: Channel

    val linkName: String
    var contactData: ContactData?
    val contactName: String
    val email: String?

    fun saveContactDetail(contactId: String, email: String, name: String)
}