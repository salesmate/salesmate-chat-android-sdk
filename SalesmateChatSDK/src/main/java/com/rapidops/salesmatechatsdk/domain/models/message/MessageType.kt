package com.rapidops.salesmatechatsdk.domain.models.message

enum class MessageType(var value: String) {
    COMMENT("comment"),
    RATING_ASKED("rating_asked"),
    EMAIL_ASKED("email_asked");

    companion object {
        fun findEnumFromValue(lookupValue: String): MessageType {
            val value = values().find { it.value.equals(lookupValue, true) }
            return value ?: COMMENT
        }
    }
}