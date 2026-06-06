package com.studysnap.data.repository

import com.studysnap.data.db.ChatMessageDao
import com.studysnap.data.entity.ChatMessage
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ChatRepository(private val chatMessageDao: ChatMessageDao) {

    fun getMessages(subjectId: String?): Flow<List<ChatMessage>> = chatMessageDao.getBySubject(subjectId)

    suspend fun sendMessage(subjectId: String?, role: String, content: String) {
        chatMessageDao.insert(ChatMessage(
            id = UUID.randomUUID().toString(),
            subjectId = subjectId,
            role = role,
            content = content
        ))
    }

    suspend fun clearHistory() = chatMessageDao.clearAll()
}