package com.studysnap.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studysnap.data.repository.QuizRepository
import com.studysnap.data.repository.SubjectRepository
import com.studysnap.ml.LlmManager
import com.studysnap.util.JsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class QuizState {
    object loading : QuizState()
    data class failed(val error: String) : QuizState()
    data class ready(val q: QuizViewModel.QuestionDisplay) : QuizState()
    data class showResult(val score: Int, val total: Int) : QuizState()
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val quizRepository: QuizRepository,
    private val llmManager: LlmManager
) : ViewModel() {

    private val _state = MutableStateFlow<QuizState>(QuizState.loading)
    val state: StateFlow<QuizState> = _state

    private var questions = listOf<QuestionDisplay>()
    private var questionIndex = 0
    private var score = 0
    private var conceptId = ""
    private var subjectId = ""

    data class QuestionDisplay(
        val index: Int, val total: Int, val question: String,
        val options: List<String>, val correctIndex: Int,
        val explanation: String, val isTF: Boolean
    )

    fun loadQuiz(cId: String, sId: String) {
        conceptId = cId; subjectId = sId
        viewModelScope.launch {
            try {
                val concept = subjectRepository.getConcept(cId) ?: run {
                    _state.value = QuizState.failed("Concept not found"); return@launch
                }
                llmManager.ensureLoaded()
                if (!llmManager.isModelLoaded()) {
                    _state.value = QuizState.failed("LLM not available"); return@launch
                }
                val result = llmManager.generateQuiz(concept.title, concept.summary)
                val json = result.getOrElse { _state.value = QuizState.failed("Quiz gen failed: ${it.message}"); return@launch }

                val root = JsonParser.parseObject(json)
                val raw = root["questions"] as? List<Map<String, Any>> ?: listOf()
                if (raw.isEmpty()) { _state.value = QuizState.failed("No questions generated"); return@launch }

                questions = raw.mapIndexed { idx, q ->
                    QuestionDisplay(
                        index = idx, total = raw.size,
                        question = q["question"] as? String ?: "",
                        options = if (q["type"] == "true_false") listOf("True", "False")
                        else (q["options"] as? List<*>)?.mapNotNull { it as? String } ?: listOf("A", "B", "C", "D"),
                        correctIndex = ((q["correct"] as? Number)?.toInt() ?: 0).coerceIn(0, 3),
                        explanation = q["explanation"] as? String ?: "",
                        isTF = q["type"] == "true_false"
                    )
                }
                questionIndex = 0; score = 0
                _state.value = QuizState.ready(questions[0])
            } catch (e: Exception) {
                _state.value = QuizState.failed(e.message ?: "Error")
            }
        }
    }

    fun answerQuestion(answerIdx: Int) {
        if (questionIndex < questions.size && answerIdx == questions[questionIndex].correctIndex) score++
    }

    fun nextQuestion(_selectedIdx: Int) {
        questionIndex++
        if (questionIndex >= questions.size) {
            viewModelScope.launch {
                quizRepository.saveResult(conceptId, subjectId, score, questions.size)
                _state.value = QuizState.showResult(score, questions.size)
            }
        } else {
            _state.value = QuizState.ready(questions[questionIndex])
        }
    }
}