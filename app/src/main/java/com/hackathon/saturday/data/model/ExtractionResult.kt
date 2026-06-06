package com.hackathon.saturday.data.model

data class ExtractionResult(
    val tasks: List<com.hackathon.saturday.data.local.entity.Task> = emptyList(),
    val deadlines: List<com.hackathon.saturday.data.local.entity.Deadline> = emptyList(),
    val events: List<com.hackathon.saturday.data.local.entity.Event> = emptyList(),
    val flashcards: List<com.hackathon.saturday.data.local.entity.Flashcard> = emptyList(),
    val rawText: String = ""
)
