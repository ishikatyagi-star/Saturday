package com.hackathon.saturday.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val question: String,
    val answer: String,
    val topic: String? = null,
    val reviewedCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val sourceText: String? = null
)
