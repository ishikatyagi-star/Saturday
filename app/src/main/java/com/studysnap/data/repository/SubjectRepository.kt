package com.studysnap.data.repository

import com.studysnap.data.db.ConceptDao
import com.studysnap.data.db.SubjectDao
import com.studysnap.data.entity.Concept
import com.studysnap.data.entity.Subject
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class SubjectRepository(
    private val subjectDao: SubjectDao,
    private val conceptDao: ConceptDao
) {
    fun getAllSubjects(): Flow<List<Subject>> = subjectDao.getAll()

    suspend fun getSubject(id: String): Subject? = subjectDao.getById(id)

    suspend fun getConcepts(subjectId: String): Flow<List<Concept>> = conceptDao.getBySubject(subjectId)

    suspend fun saveSubject(subject: Subject) = subjectDao.insert(subject)

    suspend fun saveConcepts(subjectId: String, concepts: List<Concept>) {
        conceptDao.insertAll(concepts.map {
            it.copy(subjectId = subjectId, id = it.id.ifBlank { UUID.randomUUID().toString() })
        })
    }

    suspend fun getConceptCount(subjectId: String): Int = conceptDao.countBySubject(subjectId)

    suspend fun getConcept(id: String): Concept? = conceptDao.getById(id)

    suspend fun updateComicImage(conceptId: String, base64: String) =
        conceptDao.updateComicImage(conceptId, base64)
}