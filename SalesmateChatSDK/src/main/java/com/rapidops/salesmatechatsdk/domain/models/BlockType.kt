package com.rapidops.salesmatechatsdk.domain.models

enum class BlockType(val value: String) {
    TEXT("text"),
    HTML("html"),
    ORDERED_LIST("orderedList"),
    UNORDERED_LIST("unorderedList"),
    FILE("file"),
    IMAGE("image");

    companion object {
        fun findEnumFromValue(lookupValue: String): BlockType {
            val value = values().find { it.value.equals(lookupValue, true) }
            return value ?: TEXT
        }
    }
}