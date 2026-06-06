package com.studysnap.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_messages",
    indices = [Index("subjectId")]
)
data class ChatMessage(
    @PrimaryKey val id: String,
    val subjectId: String?,
    val role: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)