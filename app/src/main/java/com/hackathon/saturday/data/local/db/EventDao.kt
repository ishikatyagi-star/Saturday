package com.hackathon.saturday.data.local.db

import androidx.room.*
import com.hackathon.saturday.data.local.entity.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE eventDate >= :startOfDay ORDER BY eventDate ASC")
    fun getUpcoming(startOfDay: Long): Flow<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<Event>)

    @Delete
    suspend fun delete(event: Event)
}
