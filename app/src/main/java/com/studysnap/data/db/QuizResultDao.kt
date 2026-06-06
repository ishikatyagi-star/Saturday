package com.studysnap.data.db

import androidx.room.*
import com.studysnap.data.entity.QuizResult
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {
    @Query("SELECT * FROM quiz_results WHERE subjectId = :subjectId ORDER BY attemptedAt DESC")
    fun getBySubject(subjectId: String): Flow<List<QuizResult>>

    @Query("SELECT * FROM quiz_results WHERE conceptId = :conceptId ORDER BY attemptedAt DESC LIMIT 1")
    suspend fun getLatestByConcept(conceptId: String): QuizResult?

    @Query("SELECT * FROM quiz_results WHERE conceptId = :conceptId ORDER BY attemptedAt DESC")
    fun getAllByConcept(conceptId: String): Flow<List<QuizResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: QuizResult)

    @Query("DELETE FROM quiz_results WHERE conceptId = :conceptId")
    suspend fun deleteByConcept(conceptId: String)
}