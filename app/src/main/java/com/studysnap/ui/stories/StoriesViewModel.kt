package com.studysnap.ui.stories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studysnap.data.entity.Concept
import com.studysnap.data.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoriesViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
) : ViewModel() {

    private val _concepts = MutableStateFlow<List<Concept>>(emptyList())
    val concepts: StateFlow<List<Concept>> = _concepts

    val subjectName = MutableStateFlow("")

    fun loadConcepts(subjectId: String) {
        viewModelScope.launch {
            subjectRepository.getSubject(subjectId)?.let { subjectName.value = it.name }
            subjectRepository.getConcepts(subjectId).collect { _concepts.value = it }
        }
    }
}