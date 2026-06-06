package com.studysnap.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class Subject(
    @PrimaryKey val id: String,
    val name: String,
    val detectedFrom: String = "ocr",
    val coverImageBase64: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)