package com.rapidops.salesmatechatsdk.domain.models

enum class FrequencyType(var value: String) {
    NEVER("never"),
    ALWAYS("always"),
    ONLY_OUTSIDE_OF_OFFICE_HOURS("only_outside_of_office_hours");

    companion object {
        fun findEnumFromValue(lookupValue: String): FrequencyType {
            val value = values().find { it.value.equals(lookupValue, true) }
            return value ?: NEVER
        }
    }
}