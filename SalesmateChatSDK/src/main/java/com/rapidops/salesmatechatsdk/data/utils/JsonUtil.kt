package com.rapidops.salesmatechatsdk.data.utils

import com.google.gson.JsonArray
import com.google.gson.JsonObject


internal fun JsonObject.getString(property: String): String? {
    if (has(property) && !get(property).isJsonNull) {
        val jsonElement = get(property)
        if (jsonElement.isJsonPrimitive) {
            val string = get(property).asString
            if (string.isNotEmpty()) return string
        }
    }
    return null
}


fun JsonObject.getInt(property: String): Int {
    getString(property)?.let {
        return it.toInt()
    }
    return 0
}

fun JsonObject.getDouble(property: String): Double {
    getString(property)?.let {
        return it.toDouble()
    }
    return 0.0
}

fun JsonObject.getBoolean(property: String): Boolean {
    getString(property)?.let {
        return it.toBoolean()
    }
    return false
}

fun JsonObject.getJsonArray(property: String): JsonArray? {
    if (has(property) && !get(property).isJsonNull) {
        val jsonElement = get(property)
        if (jsonElement.isJsonArray) return get(property).asJsonArray
    }
    return null
}

fun JsonObject.getJsonObject(property: String): JsonObject? {
    if (has(property) && !get(property).isJsonNull) {
        val jsonElement = get(property)
        if (jsonElement.isJsonObject) return get(property).asJsonObject
    }
    return null
}


fun JsonObject.hasProperty(propertyName: String): Boolean {
    return this.has(propertyName) && !this.isJsonNull
}

fun JsonObject.hasPropertyNotNull(propertyName: String): Boolean {
    return this.has(propertyName) && !this.get(propertyName).isJsonNull
}

fun JsonObject.hasProperty(jsonElement: JsonObject, propertyName: String): Boolean {
    return jsonElement.has(propertyName) && !jsonElement.isJsonNull
}