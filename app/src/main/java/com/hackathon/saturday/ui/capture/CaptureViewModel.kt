package com.hackathon.saturday.ui.capture

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hackathon.saturday.SaturdayApplication
import com.hackathon.saturday.ai.LlmInferenceEngine
import com.hackathon.saturday.ai.MlKitOcrClient
import com.hackathon.saturday.data.model.ExtractionResult
import com.hackathon.saturday.data.repository.ActionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

sealed class CaptureUiState {
    object Idle : CaptureUiState()
    object Loading : CaptureUiState()
    data class Success(val result: ExtractionResult) : CaptureUiState()
    data class Error(val message: String) : CaptureUiState()
}

class CaptureViewModel(private val repository: ActionRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<CaptureUiState>(CaptureUiState.Idle)
    val uiState: StateFlow<CaptureUiState> = _uiState.asStateFlow()

    var cameraUri: Uri? = null
    private var lastResult: ExtractionResult? = null

    fun createImageUri(context: Context): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFile = File(context.cacheDir, "JPEG_${timeStamp}.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
    }

    fun processImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.value = CaptureUiState.Loading
            try {
                val ocr = MlKitOcrClient(context)
                val rawText = ocr.processImage(uri)
                if (rawText.isBlank()) {
                    _uiState.value = CaptureUiState.Error("No text found in image")
                    return@launch
                }
                val engine = LlmInferenceEngine(context)
                val result = engine.extract(rawText)
                lastResult = result
                _uiState.value = CaptureUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = CaptureUiState.Error(e.message ?: "Extraction failed")
            }
        }
    }

    fun saveToToday() {
        viewModelScope.launch {
            lastResult?.let {
                repository.saveExtraction(it)
                _uiState.value = CaptureUiState.Idle
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CaptureViewModel(SaturdayApplication.instance.repository) as T
            }
        }
    }
}
