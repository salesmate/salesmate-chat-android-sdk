package com.rapidops.salesmatechatsdk.domain.models

internal data class ConversationDetailItem(
    var conversations: Conversations? = null,
    var user: User? = null,
) : BaseModel() {
    val isContactHasRead: Boolean
        get() {
            return if (conversations?.lastMessageData?.userId.isNullOrEmpty()) {
                true
            } else {
                conversations?.contactHasRead == true
            }
        }
}