package com.studysnap.data.db

import androidx.room.*
import com.studysnap.data.entity.Concept
import kotlinx.coroutines.flow.Flow

@Dao
interface ConceptDao {
    @Query("SELECT * FROM concepts WHERE subjectId = :subjectId ORDER BY createdAt ASC")
    fun getBySubject(subjectId: String): Flow<List<Concept>>

    @Query("SELECT * FROM concepts WHERE id = :id")
    suspend fun getById(id: String): Concept?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(concepts: List<Concept>)

    @Query("UPDATE concepts SET comicImageBase64 = :base64 WHERE id = :conceptId")
    suspend fun updateComicImage(conceptId: String, base64: String)

    @Query("SELECT COUNT(*) FROM concepts WHERE subjectId = :subjectId")
    suspend fun countBySubject(subjectId: String): Int
}