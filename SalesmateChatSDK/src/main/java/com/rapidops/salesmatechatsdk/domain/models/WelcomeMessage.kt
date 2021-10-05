package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class WelcomeMessage(
    @SerializedName("greeting_message")
    var greetingMessage: String = "",
    @SerializedName("is_default")
    var isDefault: Boolean = false,
    @SerializedName("language")
    var language: String = "",
    @SerializedName("team_intro")
    var teamIntro: String = ""
) : BaseModel()