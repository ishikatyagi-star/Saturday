package com.studysnap.seeder

import android.content.Context
import com.studysnap.data.entity.Concept
import com.studysnap.data.entity.Flashcard
import com.studysnap.data.entity.Subject
import com.studysnap.data.repository.FlashcardRepository
import com.studysnap.data.repository.SubjectRepository
import com.studysnap.ml.LlmManager
import com.studysnap.util.JsonParser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoDataSeeder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val subjectRepository: SubjectRepository,
    private val flashcardRepository: FlashcardRepository,
    private val llmManager: LlmManager
) {
    suspend fun seedIfEmpty() {
        val existing = subjectRepository.getAllSubjects()
        var hasData = false
        existing.collect { if (it.isNotEmpty()) hasData = true; return@collect }
        if (hasData) return

        withContext(Dispatchers.IO) {
            try {
                seedFallback()
            } catch (_: Exception) {
                seedFallback()
            }
        }
    }

    private suspend fun seedFallback() {
        val subject = Subject(id = "demo_physics_nlm", name = "Physics — Newton's Laws", detectedFrom = "seeder")
        subjectRepository.saveSubject(subject)

        val concepts = listOf(
            Concept(
                id = "concept_nlm_1", subjectId = subject.id,
                title = "Newton's First Law (Inertia)",
                summary = "An object at rest stays at rest unless acted upon by an unbalanced force. This is called the law of inertia.",
                keyPoints = "Objects resist changes in motion, More mass means more inertia, Net force of zero means constant velocity",
                analogy = "Like a shopping cart — full cart harder to start and harder to stop."
            ),
            Concept(
                id = "concept_nlm_2", subjectId = subject.id,
                title = "Newton's Second Law (F = ma)",
                summary = "The acceleration of an object is directly proportional to net force and inversely proportional to mass. F = ma.",
                keyPoints = "F = ma, Acceleration direction = net force direction, Unit: Newton (kg·m/s²), Double force = double acceleration",
                analogy = "Pushing a bicycle vs. a car — same push makes bicycle accelerate much faster."
            ),
            Concept(
                id = "concept_nlm_3", subjectId = subject.id,
                title = "Newton's Third Law (Action-Reaction)",
                summary = "For every action, there is an equal and opposite reaction. Forces come in pairs acting on different objects.",
                keyPoints = "Action-reaction pairs, Equal magnitude opposite direction, Act on different objects, Do not cancel",
                analogy = "Like jumping off a boat — you push the boat back, your body moves forward."
            )
        )
        subjectRepository.saveConcepts(subject.id, concepts)

        val flashcardSets = mapOf(
            "concept_nlm_1" to listOf(
                Flashcard("fc_nlm_1", "concept_nlm_1", "What is inertia?", "The tendency of an object to resist changes in its state of motion.", "easy"),
                Flashcard("fc_nlm_2", "concept_nlm_1", "True/False: More mass means less inertia.", "False. More mass means MORE inertia.", "easy"),
                Flashcard("fc_nlm_3", "concept_nlm_1", "What happens when net force is zero?", "Constant velocity — object stays at rest or moves at constant speed in straight line.", "medium")
            ),
            "concept_nlm_2" to listOf(
                Flashcard("fc_nlm_4", "concept_nlm_2", "What is F = ma?", "Force = mass × acceleration. Core equation of Newton's Second Law.", "easy"),
                Flashcard("fc_nlm_5", "concept_nlm_2", "What is the unit of force?", "Newton (N) = kg·m/s²", "medium"),
                Flashcard("fc_nlm_6", "concept_nlm_2", "If force doubles and mass stays same, what happens to acceleration?", "Acceleration doubles (a ∝ F).", "medium")
            ),
            "concept_nlm_3" to listOf(
                Flashcard("fc_nlm_7", "concept_nlm_3", "Do action-reaction forces cancel?", "No — they act on DIFFERENT objects.", "medium"),
                Flashcard("fc_nlm_8", "concept_nlm_3", "Give an example of Newton's Third Law.", "Walking: foot pushes ground back, ground pushes foot forward.", "easy"),
                Flashcard("fc_nlm_9", "concept_nlm_3", "How do rockets work in space?", "Rocket pushes exhaust backward (action), gases push rocket forward (reaction). No atmosphere needed.", "hard")
            )
        )

        flashcardSets.forEach { (conceptId, cards) ->
            flashcardRepository.saveFlashcards(conceptId, cards)
        }
    }
}