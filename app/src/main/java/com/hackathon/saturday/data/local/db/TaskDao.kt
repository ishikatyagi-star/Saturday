package com.hackathon.saturday.data.local.db

import androidx.room.*
import com.hackathon.saturday.data.local.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC, priority DESC")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND (dueDate IS NULL OR dueDate >= :startOfDay) ORDER BY dueDate ASC, priority DESC")
    fun getPending(startOfDay: Long): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<Task>)

    @Delete
    suspend fun delete(task: Task)

    @Query("UPDATE tasks SET isCompleted = :completed WHERE id = :id")
    suspend fun updateCompletion(id: Long, completed: Boolean)
}
