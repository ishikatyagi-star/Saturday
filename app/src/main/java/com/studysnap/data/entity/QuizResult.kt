package com.studysnap.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "quiz_results",
    foreignKeys = [
        ForeignKey(entity = Concept::class, parentColumns = ["id"], childColumns = ["conceptId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Subject::class, parentColumns = ["id"], childColumns = ["subjectId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("conceptId"), Index("subjectId")]
)
data class QuizResult(
    @PrimaryKey val id: String,
    val conceptId: String,
    val subjectId: String,
    val score: Int,
    val totalQuestions: Int,
    val attemptedAt: Long = System.currentTimeMillis()
)