package com.hackathon.saturday.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hackathon.saturday.data.local.entity.Task
import com.hackathon.saturday.data.local.entity.Deadline
import com.hackathon.saturday.data.local.entity.Event
import com.hackathon.saturday.data.local.entity.Flashcard

@Database(
    entities = [Task::class, Deadline::class, Event::class, Flashcard::class],
    version = 1,
    exportSchema = false
)
abstract class SaturdayDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun deadlineDao(): DeadlineDao
    abstract fun eventDao(): EventDao
    abstract fun flashcardDao(): FlashcardDao

    companion object {
        @Volatile
        private var INSTANCE: SaturdayDatabase? = null

        fun getInstance(context: Context): SaturdayDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SaturdayDatabase::class.java,
                    "saturday_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
