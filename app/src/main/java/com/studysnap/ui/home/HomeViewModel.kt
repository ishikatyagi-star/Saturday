package com.studysnap.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studysnap.data.entity.Subject
import com.studysnap.data.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
) : ViewModel() {

    val subjects: StateFlow<List<Subject>> = subjectRepository.getAllSubjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val scannedCount: StateFlow<Map<String, Pair<Int, Int>>> = subjects.map { subjectList ->
        val map = mutableMapOf<String, Pair<Int, Int>>()
        subjectList.forEach { s ->
            val count = try {
                subjectRepository.getConceptCount(s.id)
            } catch (_: Exception) { 0 }
            map[s.id] = Pair(0, count) // scanned = 0 until quizzes are done, conceptCount = count
        }
        map
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
}