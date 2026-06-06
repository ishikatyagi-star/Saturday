package com.studysnap.util

import org.json.JSONArray
import org.json.JSONObject

object JsonParser {
    fun parseObject(json: String): Map<String, Any> {
        val obj = JSONObject(json)
        return obj.toMap()
    }

    private fun JSONObject.toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        keys().forEach { key ->
            map[key] = when (val v = get(key)) {
                is JSONObject -> v.toMap()
                is JSONArray -> v.toList()
                else -> v
            }
        }
        return map
    }

    private fun JSONArray.toList(): List<Any> {
        return (0 until length()).map { i ->
            when (val v = get(i)) {
                is JSONObject -> v.toMap()
                is JSONArray -> v.toList()
                else -> v
            }
        }
    }

    fun safeStringKey(map: Map<String, Any>, key: String): String? =
        (map[key] as? String)?.takeIf { it.isNotBlank() }

    fun safeDoubleKey(map: Map<String, Any>, key: String): Double? =
        (map[key] as? Number)?.toDouble()
}