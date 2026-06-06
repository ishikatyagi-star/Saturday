package com.studysnap.ui.flashcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studysnap.data.entity.Flashcard
import com.studysnap.data.repository.FlashcardRepository
import com.studysnap.data.repository.SubjectRepository
import com.studysnap.ml.LlmManager
import com.studysnap.util.JsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed class FlashcardState {
    object loading : FlashcardState()
    object empty : FlashcardState()
    data class active(val card: FlashcardsViewModel.CardDisplay) : FlashcardState()
    object done : FlashcardState()
}

@HiltViewModel
class FlashcardsViewModel @Inject constructor(
    private val flashcardRepository: FlashcardRepository,
    private val subjectRepository: SubjectRepository,
    private val llmManager: LlmManager
) : ViewModel() {

    data class CardDisplay(val id: String, val front: String, val back: String, val difficulty: String)

    private val _state = MutableStateFlow<FlashcardState>(FlashcardState.loading)
    val state: StateFlow<FlashcardState> = _state

    private var cards = listOf<Flashcard>()
    private var cardIndex = 0

    fun loadCards(conceptId: String) {
        viewModelScope.launch {
            flashcardRepository.getByConcept(conceptId).collect { list ->
                cards = list.sortedBy { it.nextReviewAt }
                cardIndex = 0
                _state.value = if (cards.isEmpty()) FlashcardState.empty else FlashcardState.active(cards[0].toDisplay())
                return@collect
            }
        }
    }

    fun generateCards(conceptId: String) {
        viewModelScope.launch {
            val concept = subjectRepository.getConcept(conceptId)
            if (concept == null || !llmManager.isModelLoaded()) {
                _state.value = FlashcardState.empty; return@launch
            }
            val result = llmManager.generateFlashcards(concept.title, concept.keyPoints)
            val json = result.getOrNull() ?: run { _state.value = FlashcardState.empty; return@launch }
            val root = JsonParser.parseObject(json)
            val raw = root["flashcards"] as? List<Map<String, Any>> ?: emptyList()
            val newCards = raw.map { c ->
                Flashcard(
                    id = UUID.randomUUID().toString(), conceptId = conceptId,
                    front = c["front"] as? String ?: "", back = c["back"] as? String ?: "",
                    difficulty = c["difficulty"] as? String ?: "medium"
                )
            }
            flashcardRepository.saveFlashcards(conceptId, newCards)
            loadCards(conceptId)
        }
    }

    fun swipeRight() {
        val card = cards.getOrNull(cardIndex) ?: return
        viewModelScope.launch { flashcardRepository.recordSwipe(card, true) }
        advance()
    }

    fun swipeLeft() {
        val card = cards.getOrNull(cardIndex) ?: return
        viewModelScope.launch { flashcardRepository.recordSwipe(card, false) }
        advance()
    }

    private fun advance() {
        cardIndex++
        if (cardIndex >= cards.size) _state.value = FlashcardState.done
        else _state.value = FlashcardState.active(cards[cardIndex].toDisplay())
    }

    fun reload(conceptId: String) {
        loadCards(conceptId)
    }

    fun unload() {
        // Back navigation handled by NavController
    }

    private fun Flashcard.toDisplay() = CardDisplay(id, front, back, difficulty)
}