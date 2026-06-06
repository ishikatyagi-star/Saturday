package com.studysnap.data.db

import androidx.room.*
import com.studysnap.data.entity.Flashcard
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards WHERE conceptId = :conceptId ORDER BY nextReviewAt ASC")
    fun getByConcept(conceptId: String): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE nextReviewAt <= :now ORDER BY masteryScore ASC, nextReviewAt ASC")
    suspend fun getDueCards(now: Long): List<Flashcard>

    @Query("SELECT * FROM flashcards WHERE id = :id")
    suspend fun getById(id: String): Flashcard?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(flashcards: List<Flashcard>)

    @Update
    suspend fun update(flashcard: Flashcard)

    @Query("DELETE FROM flashcards WHERE conceptId = :conceptId")
    suspend fun deleteByConcept(conceptId: String)
}