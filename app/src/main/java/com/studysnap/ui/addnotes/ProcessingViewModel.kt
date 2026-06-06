package com.studysnap.ui.addnotes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studysnap.data.entity.Concept
import com.studysnap.data.entity.Subject
import com.studysnap.data.repository.SubjectRepository
import com.studysnap.ml.LlmManager
import com.studysnap.ml.OcrManager
import com.studysnap.util.JsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProcessingViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val ocrManager: OcrManager,
    private val llmManager: LlmManager,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _currentStep = MutableStateFlow<ProcessingStep>(ProcessingStep.Ocr)
    val currentStep: StateFlow<ProcessingStep> = _currentStep

    fun startProcessing(context: Context) {
        viewModelScope.launch {
            try {
                _currentStep.value = ProcessingStep.Ocr
                delay(300)

                val file = java.io.File(appContext.cacheDir, "processing_input.txt")
                val rawText = if (file.exists()) file.readText() else ""

                if (rawText.isBlank()) {
                    _currentStep.value = ProcessingStep.Error("No text found. Try the demo data first.")
                    return@launch
                }

                _currentStep.value = ProcessingStep.Llm
                delay(300)

                llmManager.ensureLoaded()
                if (!llmManager.isModelLoaded()) {
                    _currentStep.value = ProcessingStep.Error("LLM model not loaded.\nPlace gemma3-1b-it-int4.task in assets/ or use demo data.")
                    return@launch
                }

                val result = llmManager.extractConcepts(rawText)
                val json = result.getOrElse {
                    _currentStep.value = ProcessingStep.Error("AI failed: ${it.message}")
                    return@launch
                }

                _currentStep.value = ProcessingStep.Store
                delay(300)

                saveConcepts(json)
                _currentStep.value = ProcessingStep.Done
            } catch (e: Exception) {
                _currentStep.value = ProcessingStep.Error(e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun saveConcepts(json: String) {
        val root = JsonParser.parseObject(json)
        val subjectName = root["subject"] as? String ?: "Untitled"
        val subjectId = UUID.randomUUID().toString()
        subjectRepository.saveSubject(Subject(id = subjectId, name = subjectName, detectedFrom = "ocr"))

        val conceptsRaw = root["concepts"] as? List<Map<String, Any>> ?: emptyList()
        val concepts = conceptsRaw.map { c ->
            Concept(
                id = (c["id"] as? String) ?: UUID.randomUUID().toString(),
                subjectId = subjectId,
                title = c["title"] as? String ?: "Concept",
                summary = c["summary"] as? String ?: "",
                keyPoints = (c["key_points"] as? List<*>)?.joinToString(", ") ?: "",
                analogy = c["analogy"] as? String ?: ""
            )
        }
        subjectRepository.saveConcepts(subjectId, concepts)
    }
}