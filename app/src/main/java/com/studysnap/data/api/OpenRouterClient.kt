package com.studysnap.data.api

import com.studysnap.AppConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenRouterClient @Inject constructor() {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun generateImage(prompt: String): Result<String?> {
        if (AppConfig.openRouterKey.isBlank() || AppConfig.openRouterKey == "your_openrouter_api_key_here") {
            return Result.failure(Exception("OpenRouter API key not configured"))
        }
        return try {
            val body = JSONObject().apply {
                put("model", AppConfig.OPENROUTER_IMAGE_MODEL)
                put("messages", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", prompt)
                    })
                })
                put("max_tokens", 2048)
            }

            val requestBody = body.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(AppConfig.OPENROUTER_BASE_URL)
                .addHeader("Authorization", "Bearer ${AppConfig.openRouterKey}")
                .addHeader("Content-Type", "application/json")
                .addHeader("HTTP-Referer", "https://studysnap.app")
                .addHeader("X-Title", "StudySnap")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                Result.failure(Exception("OpenRouter error ${response.code}"))
            } else {
                val responseBody = response.body?.string() ?: ""
                val base64 = extractBase64Image(responseBody)
                Result.success(base64)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun extractBase64Image(responseJson: String): String? {
        return try {
            val root = JSONObject(responseJson)
            val choices = root.getJSONArray("choices")
            val message = choices.getJSONObject(0).getJSONObject("message")
            val content = message.get("content")
            when (content) {
                is String -> content.lines()
                    .firstOrNull { it.startsWith("data:image") }
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }
}