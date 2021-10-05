package com.rapidops.salesmatechatsdk.core

import com.rapidops.salesmatechatsdk.domain.models.BuildType

class SalesmateChatSettings(
    var workspaceId: String,
    var appKey: String,
    var tenantId: String,
    var buildType: BuildType = BuildType.DEVELOPMENT
)