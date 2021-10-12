package com.rapidops.salesmatechatsdk.domain.datasources

import com.rapidops.salesmatechatsdk.core.SalesmateChatSettings
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.domain.models.Channel

internal interface IAppSettingsDataSource {

    var salesMateChatSetting: SalesmateChatSettings

    var androidUniqueId: String

    var accessToken: String
    var pseudoName: String

    var pingRes: PingRes
    var channel: Channel

    val linkName: String
}