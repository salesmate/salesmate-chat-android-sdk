package com.rapidops.salesmatechatsdk.domain.models

enum class ConversationStatus(val value: String) {
    NONE("NONE"),
    OPEN("open"),
    CLOSED("closed");

    companion object {
        fun findEnumFromValue(lookupValue: String): ConversationStatus {
            val value = values().find { it.value.equals(lookupValue, true) }
            return value ?: NONE
        }
    }
}