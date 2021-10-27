package com.rapidops.salesmatechatsdk.domain.models

enum class PublishType(val value: String) {
    NONE("NONE"),
    NEW_MESSAGE("NEW_MESSAGE"),
    USER_AVAILABILITY_STATUS_UPDATED("USER_AVAILABILITY_STATUS_UPDATED"),
    CONVERSATION_HAS_READ("CONVERSATION_HAS_READ"),
    CONVERSATION_STATUS_UPDATE("CONVERSATION_STATUS_UPDATE"),
    MESSAGE_DELETED("MESSAGE_DELETED"),
    UPDATE_CONVERSATIONS_LIST("UPDATE_CONVERSATIONS_LIST"),
    CONVERSATION_RATING_CHANGED("CONVERSATION_RATING_CHANGED"),
    CONVERSATION_REMARK_ADDED("CONVERSATION_REMARK_ADDED");

    companion object {
        fun findEnumFromValue(lookupValue: String): PublishType {
            val value = values().find { it.value.equals(lookupValue, true) }
            return value ?: NONE
        }
    }
}