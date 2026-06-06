package com.hackathon.saturday.data.model

import com.hackathon.saturday.data.local.entity.Task
import com.hackathon.saturday.data.local.entity.Deadline
import com.hackathon.saturday.data.local.entity.Event
import com.hackathon.saturday.data.local.entity.Flashcard

sealed class TodayItem(val timestamp: Long) {
    data class TodayTask(val task: Task) : TodayItem(task.dueDate ?: task.createdAt)
    data class TodayDeadline(val deadline: Deadline) : TodayItem(deadline.dueDate)
    data class TodayEvent(val event: Event) : TodayItem(event.eventDate)
	data class TodayFlashcard(val flashcard: Flashcard) : TodayItem(flashcard.createdAt)
}
