package com.hackathon.saturday.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hackathon.saturday.SaturdayApplication
import com.hackathon.saturday.data.model.TodayItem
import com.hackathon.saturday.data.repository.ActionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TodayViewModel(private val repository: ActionRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val todayItems: StateFlow<List<TodayItem>> = repository
        .getTodayItems(startOfDay())
        .onEach { _isLoading.value = false }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun completeItem(item: TodayItem) {
        viewModelScope.launch {
            when (item) {
                is TodayItem.TodayTask -> repository.completeTask(item.task.id)
                is TodayItem.TodayDeadline -> repository.completeDeadline(item.deadline.id)
                else -> {}
            }
        }
    }

    private fun startOfDay(): Long {
        val cal = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TodayViewModel(SaturdayApplication.instance.repository) as T
            }
        }
    }
}
