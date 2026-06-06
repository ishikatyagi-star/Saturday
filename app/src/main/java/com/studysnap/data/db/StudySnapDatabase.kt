package com.studysnap.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.studysnap.data.entity.*

@Database(
    entities = [Subject::class, Concept::class, QuizResult::class, Flashcard::class, ChatMessage::class],
    version = 1,
    exportSchema = false
)
abstract class StudySnapDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun conceptDao(): ConceptDao
    abstract fun quizResultDao(): QuizResultDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun chatMessageDao(): ChatMessageDao
}