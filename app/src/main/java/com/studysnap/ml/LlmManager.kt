package com.studysnap.ml

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.studysnap.AppConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LlmManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var llmInference: LlmInference? = null
    private var isLoaded = false

    suspend fun ensureLoaded() {
        if (isLoaded) return
        withContext(Dispatchers.IO) {
            try {
                val options = LlmInference.LlmInferenceOptions.builder()
                    .setModelPath(AppConfig.GEMMA_MODEL_PATH)
                    .setMaxTokens(AppConfig.MAX_TOKENS)
                    .setTemperature(AppConfig.MODEL_TEMPERATURE)
                    .build()
                llmInference = LlmInference.createFromOptions(context, options)
                isLoaded = true
            } catch (e: Exception) {
                isLoaded = false
            }
        }
    }

    fun isModelLoaded(): Boolean = isLoaded

    suspend fun extractConcepts(rawText: String): Result<String> {
        return generate(PromptTemplates.conceptExtraction(rawText))
    }

    suspend fun generateQuiz(conceptTitle: String, conceptSummary: String): Result<String> {
        return generate(PromptTemplates.quizGeneration(conceptTitle, conceptSummary))
    }

    suspend fun generateFlashcards(conceptTitle: String, keyPoints: String): Result<String> {
        return generate(PromptTemplates.flashcardGeneration(conceptTitle, keyPoints))
    }

    suspend fun chatReply(
        userMessage: String,
        notesContext: String,
        weakAreas: String,
        strongAreas: String
    ): Result<String> {
        val systemPrompt = PromptTemplates.chatbotSystemPrompt(notesContext, weakAreas, strongAreas)
        val fullPrompt = "$systemPrompt\n\nUser: $userMessage\nAssistant:"
        return generate(fullPrompt)
    }

    private suspend fun generate(prompt: String): Result<String> = withContext(Dispatchers.IO) {
        if (!isLoaded) return@withContext Result.failure(IllegalStateException("LLM not loaded"))
        try {
            val result = llmInference?.generateResponse(prompt) ?: ""
            if (result.isBlank()) Result.failure(Exception("Empty LLM response"))
            else Result.success(extractJson(result))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun extractJson(raw: String): String {
        var text = raw.trim()
        if (text.contains("```json")) {
            text = text.substringAfter("```json").substringBefore("```").trim()
        } else if (text.contains("```")) {
            text = text.substringAfter("```").substringBefore("```").trim()
        }
        if (text.startsWith("{") || text.startsWith("[")) return text
        val start = text.indexOfAny(charArrayOf('{', '['))
        if (start == -1) return text
        val end = text.lastIndexOfAny(charArrayOf('}', ']'))
        if (end == -1) return text
        return text.substring(start, end + 1)
    }

    fun close() {
        llmInference?.close()
        llmInference = null
        isLoaded = false
    }
}