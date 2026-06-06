package com.studysnap.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studysnap.data.entity.ChatMessage
import com.studysnap.data.repository.ChatRepository
import com.studysnap.data.repository.SubjectRepository
import com.studysnap.ml.LlmManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val subjectRepository: SubjectRepository,
    private val llmManager: LlmManager
) : ViewModel() {

    data class MessageDisplay(val role: String, val content: String, val timestamp: Long)

    private var subjectId: String? = null
    private var notesContext = ""
    private var weakAreas = "None"
    private var strongAreas = "None"

    val messages: StateFlow<List<MessageDisplay>> = chatRepository.getMessages(null)
        .map { it.map { msg -> MessageDisplay(msg.role, msg.content, msg.timestamp) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadContext(sId: String) {
        subjectId = sId
        viewModelScope.launch {
            subjectRepository.getSubject(sId)?.let { subject ->
                notesContext = ""
                subjectRepository.getConcepts(sId).collect { concepts ->
                    notesContext = concepts.joinToString("\n") { c ->
                        "${c.title}: ${c.summary}\nKey points: ${c.keyPoints}"
                    }
                }
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            chatRepository.sendMessage(subjectId, "user", text)
            llmManager.ensureLoaded()
            if (!llmManager.isModelLoaded()) {
                chatRepository.sendMessage(subjectId, "assistant", "I'm still loading my knowledge. Try again in a moment.")
                return@launch
            }
            val result = llmManager.chatReply(text, notesContext, weakAreas, strongAreas)
            val reply = result.getOrElse { "I'm sorry, I couldn't process that. Try again." }
            chatRepository.sendMessage(subjectId, "assistant", reply)
        }
    }

    fun navBack() {}
}