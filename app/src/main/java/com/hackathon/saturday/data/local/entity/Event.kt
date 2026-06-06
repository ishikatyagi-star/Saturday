package com.hackathon.saturday.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val location: String? = null,
    val eventDate: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val sourceText: String? = null
)
