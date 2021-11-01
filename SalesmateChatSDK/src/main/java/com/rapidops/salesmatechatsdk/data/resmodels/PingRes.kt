package com.rapidops.salesmatechatsdk.data.resmodels


import com.google.gson.annotations.SerializedName
import com.rapidops.salesmatechatsdk.domain.models.*

internal data class PingRes(
    @SerializedName("availability")
    var availability: Availability? = null,
    @SerializedName("canVisitorOrContactStartNewConversation")
    var canVisitorOrContactStartNewConversation: Boolean = false,
    @SerializedName("contactData")
    var contactData: ContactData? = null,
    @SerializedName("conversationsSettings")
    var conversationsSettings: ConversationsSettings? = null,
    @SerializedName("emojiMapping")
    var emojiMapping: List<EmojiMapping> = listOf(),
    @SerializedName("linkname")
    var linkname: String = "",
    @SerializedName("lookAndFeel")
    var lookAndFeel: LookAndFeel = LookAndFeel(),
    @SerializedName("misc")
    var misc: Misc? = null,
    @SerializedName("securitySettings")
    var securitySettings: SecuritySettings? = null,
    @SerializedName("unReadConversations")
    var unReadConversations: List<String> = listOf(),
    @SerializedName("upfrontEmailCollection")
    var upfrontEmailCollection: UpfrontEmailCollection? = null,
    @SerializedName("users")
    var users: List<User> = listOf(),
    @SerializedName("welcomeMessages")
    var welcomeMessages: List<WelcomeMessage> = listOf(),
    @SerializedName("workspaceData")
    var workspaceData: WorkspaceData? = null
) : BaseRes(){

    fun getAvailableUserList(): List<User> {
        return users.filter { it.status == AvailabilityStatus.AVAILABLE.value }
    }
}