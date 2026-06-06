package com.hackathon.saturday.data.local.db

import androidx.room.*
import com.hackathon.saturday.data.local.entity.Deadline
import kotlinx.coroutines.flow.Flow

@Dao
interface DeadlineDao {
    @Query("SELECT * FROM deadlines WHERE isCompleted = 0 AND dueDate >= :startOfDay ORDER BY dueDate ASC, priority DESC")
    fun getUpcoming(startOfDay: Long): Flow<List<Deadline>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deadline: Deadline): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(deadlines: List<Deadline>)

    @Delete
    suspend fun delete(deadline: Deadline)

    @Query("UPDATE deadlines SET isCompleted = :completed WHERE id = :id")
    suspend fun updateCompletion(id: Long, completed: Boolean)
}
