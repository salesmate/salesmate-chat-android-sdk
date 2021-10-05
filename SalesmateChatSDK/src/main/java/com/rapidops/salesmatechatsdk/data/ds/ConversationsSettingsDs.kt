package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getBoolean
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.ConversationsSettings
import java.lang.reflect.Type

internal class ConversationsSettingsDs : JsonDeserializer<ConversationsSettings> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ConversationsSettings {

        val conversationsSettings = ConversationsSettings()

        val jsonObject = json.asJsonObject

        conversationsSettings.id = jsonObject.getString("id")?:""
        conversationsSettings.linkname = jsonObject.getString("linkname")?:""
        conversationsSettings.workspaceId = jsonObject.getString("workspace_id")?:""
        conversationsSettings.preventRepliesToCloseConversationsForContacts = jsonObject.getBoolean("prevent_replies_to_close_conversations_for_contacts")
        conversationsSettings.preventRepliesToCloseConversationsForVisitors = jsonObject.getBoolean("prevent_replies_to_close_conversations_for_visitors")
        conversationsSettings.preventRepliesToCloseConversationsWithinNumberOfDaysForContacts = jsonObject.getString("prevent_replies_to_close_conversations_within_number_of_days_for_contacts")?:""
        conversationsSettings.preventRepliesToCloseConversationsWithinNumberOfDaysForVisitors = jsonObject.getString("prevent_replies_to_close_conversations_within_number_of_days_for_visitors")?:""
        conversationsSettings.allowStartingNewConversationForContacts = jsonObject.getBoolean("allow_starting_new_conversation_for_contacts")
        conversationsSettings.allowStartingNewConversationForVisitors = jsonObject.getBoolean("allow_starting_new_conversation_for_visitors")
        conversationsSettings.allowAllContacts = jsonObject.getBoolean("allow_all_contacts")
        conversationsSettings.allowAllVisitors = jsonObject.getBoolean("allow_all_visitors")
        conversationsSettings.inboundConversationsForContactsPredicateGroup = jsonObject.get("inbound_conversations_for_contacts_predicate_group")
        conversationsSettings.inboundConversationsForVisitorsPredicateGroup = jsonObject.get("inbound_conversations_for_visitors_predicate_group")

        return conversationsSettings
    }
}