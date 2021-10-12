package com.rapidops.salesmatechatsdk.domain.models

enum class AvailabilityStatus(val value: String) {
    NONE("NONE"),
    AVAILABLE("available"),
    AWAY("away");

    companion object {
        fun findEnumFromValue(lookupValue: String): AvailabilityStatus {
            val value = values().find { it.value.equals(lookupValue, true) }
            return value ?: NONE
        }
    }
}