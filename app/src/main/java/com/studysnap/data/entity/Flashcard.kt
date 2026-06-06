package com.studysnap.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "flashcards",
    foreignKeys = [ForeignKey(
        entity = Concept::class,
        parentColumns = ["id"],
        childColumns = ["conceptId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("conceptId")]
)
data class Flashcard(
    @PrimaryKey val id: String,
    val conceptId: String,
    val front: String,
    val back: String,
    val difficulty: String = "medium",
    val swipeHistory: String = "[]",
    val nextReviewAt: Long = System.currentTimeMillis(),
    val masteryScore: Float = 0f
)