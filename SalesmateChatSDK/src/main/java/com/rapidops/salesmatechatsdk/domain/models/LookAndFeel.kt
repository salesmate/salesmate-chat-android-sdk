package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class LookAndFeel(
    @SerializedName("action_color")
    var actionColor: String = "",
    @SerializedName("background_color")
    var backgroundColor: String = "",
    @SerializedName("bottom_spacing")
    var bottomSpacing: String = "",
    @SerializedName("launcher_position")
    var launcherPosition: String = "",
    @SerializedName("logo_url")
    var logoUrl: String = "",
    @SerializedName("messenger_background")
    var messengerBackground: String = "",
    @SerializedName("show_powered_by")
    var showPoweredBy: Boolean = false,
    @SerializedName("side_spacing")
    var sideSpacing: String = ""
) : BaseModel()