package com.studysnap.data.db

import androidx.room.*
import com.studysnap.data.entity.ChatMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages WHERE subjectId IS NULL OR subjectId = :subjectId ORDER BY timestamp ASC")
    fun getBySubject(subjectId: String?): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: ChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun clearAll()
}