package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class ConversationsSettings(
    @SerializedName("allow_all_contacts")
    var allowAllContacts: Boolean = false,
    @SerializedName("allow_all_visitors")
    var allowAllVisitors: Boolean = false,
    @SerializedName("allow_starting_new_conversation_for_contacts")
    var allowStartingNewConversationForContacts: Boolean = false,
    @SerializedName("allow_starting_new_conversation_for_visitors")
    var allowStartingNewConversationForVisitors: Boolean = false,
    @SerializedName("id")
    var id: String = "",
    @SerializedName("inbound_conversations_for_contacts_predicate_group")
    var inboundConversationsForContactsPredicateGroup: Any? = null,
    @SerializedName("inbound_conversations_for_visitors_predicate_group")
    var inboundConversationsForVisitorsPredicateGroup: Any? = null,
    @SerializedName("linkname")
    var linkname: String = "",
    @SerializedName("prevent_replies_to_close_conversations_for_contacts")
    var preventRepliesToCloseConversationsForContacts: Boolean = false,
    @SerializedName("prevent_replies_to_close_conversations_for_visitors")
    var preventRepliesToCloseConversationsForVisitors: Boolean = false,
    @SerializedName("prevent_replies_to_close_conversations_within_number_of_days_for_contacts")
    var preventRepliesToCloseConversationsWithinNumberOfDaysForContacts: String = "",
    @SerializedName("prevent_replies_to_close_conversations_within_number_of_days_for_visitors")
    var preventRepliesToCloseConversationsWithinNumberOfDaysForVisitors: String = "",
    @SerializedName("workspace_id")
    var workspaceId: String = ""
) : BaseModel()