package com.studysnap.data.repository

import com.studysnap.data.db.QuizResultDao
import com.studysnap.data.db.SubjectDao
import com.studysnap.data.entity.QuizResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class QuizRepository(
    private val quizResultDao: QuizResultDao,
    private val subjectDao: SubjectDao
) {
    fun getResultsBySubject(subjectId: String): Flow<List<QuizResult>> =
        quizResultDao.getBySubject(subjectId)

    suspend fun getLatestByConcept(conceptId: String): QuizResult? =
        quizResultDao.getLatestByConcept(conceptId)

    suspend fun saveResult(conceptId: String, subjectId: String, score: Int, total: Int) {
        quizResultDao.insert(QuizResult(
            id = UUID.randomUUID().toString(),
            conceptId = conceptId,
            subjectId = subjectId,
            score = score,
            totalQuestions = total
        ))
    }

    suspend fun getWeakConcepts(subjectId: String): List<String> {
        val subjects = subjectDao.getAll() // placeholder
        return emptyList() // simplified; computed from results in VM
    }
}