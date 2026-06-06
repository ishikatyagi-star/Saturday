package com.studysnap.data.repository

import com.studysnap.data.db.FlashcardDao
import com.studysnap.data.db.QuizResultDao
import com.studysnap.data.entity.Flashcard
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class FlashcardRepository(
    private val flashcardDao: FlashcardDao,
    private val quizResultDao: QuizResultDao
) {
    fun getByConcept(conceptId: String): Flow<List<Flashcard>> = flashcardDao.getByConcept(conceptId)

    suspend fun getDueCards(): List<Flashcard> = flashcardDao.getDueCards(System.currentTimeMillis())

    suspend fun saveFlashcards(conceptId: String, flashcards: List<Flashcard>) {
        flashcardDao.insertAll(flashcards.map {
            it.copy(conceptId = conceptId, id = it.id.ifBlank { UUID.randomUUID().toString() })
        })
    }

    suspend fun recordSwipe(card: Flashcard, gotIt: Boolean) {
        val history = try {
            val current = com.studysnap.util.JsonParser.parseObject("{\"h\":${card.swipeHistory}}")["h"] as? List<Any>
            (current?.mapNotNull { it as? String } ?: emptyList()) + if (gotIt) "right" else "left"
        } catch (_: Exception) { listOf(if (gotIt) "right" else "left") }

        val newMastery = if (gotIt)
            (card.masteryScore + 0.15f).coerceAtMost(1f)
        else
            (card.masteryScore - 0.1f).coerceAtLeast(0f)

        val nextInterval = when {
            gotIt && newMastery > 0.8f -> 7 * 24 * 60 * 60 * 1000L
            gotIt && newMastery > 0.5f -> 3 * 24 * 60 * 60 * 1000L
            gotIt -> 24 * 60 * 60 * 1000L
            else -> 1 * 60 * 60 * 1000L
        }

        flashcardDao.update(card.copy(
            swipeHistory = history.joinToString(",") { "\"$it\"" }.let { "[$it]" },
            masteryScore = newMastery,
            nextReviewAt = System.currentTimeMillis() + nextInterval
        ))
    }
}