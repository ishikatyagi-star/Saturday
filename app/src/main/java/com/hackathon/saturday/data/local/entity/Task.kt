package com.hackathon.saturday.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val course: String? = null,
    val dueDate: Long? = null,
    val priority: Int = 0, // 0=normal, 1=high, 2=urgent
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val sourceText: String? = null
)
