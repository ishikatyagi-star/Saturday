package com.studysnap.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "concepts",
    foreignKeys = [ForeignKey(
        entity = Subject::class,
        parentColumns = ["id"],
        childColumns = ["subjectId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("subjectId")]
)
data class Concept(
    @PrimaryKey val id: String,
    val subjectId: String,
    val title: String,
    val summary: String,
    val keyPoints: String,
    val analogy: String,
    val comicImageBase64: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)