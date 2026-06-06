package com.studysnap.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studysnap.data.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
) : ViewModel() {
    val conceptTitle = MutableStateFlow("")

    fun loadConcept(conceptId: String) {
        viewModelScope.launch {
            subjectRepository.getConcept(conceptId)?.let { conceptTitle.value = it.title }
        }
    }
}