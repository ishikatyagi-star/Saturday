package com.studysnap

object AppConfig {
    val openRouterKey: String
        get() = BuildConfig.OPENROUTER_API_KEY

    const val OPENROUTER_BASE_URL = "https://openrouter.ai/api/v1/chat/completions"
    const val OPENROUTER_IMAGE_MODEL = "google/gemini-2.0-flash-preview-image-generation"
    const val GEMMA_MODEL_PATH = "gemma3-1b-it-int4.task"
    const val MAX_TOKENS = 1024
    const val MODEL_TEMPERATURE = 0.3f
}