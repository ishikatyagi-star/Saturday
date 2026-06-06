package com.studysnap.di

import com.studysnap.data.db.*
import com.studysnap.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides @Singleton
    fun provideSubjectRepository(
        subjectDao: SubjectDao, conceptDao: ConceptDao
    ): SubjectRepository = SubjectRepository(subjectDao, conceptDao)

    @Provides @Singleton
    fun provideQuizRepository(
        quizResultDao: QuizResultDao, subjectDao: SubjectDao
    ): QuizRepository = QuizRepository(quizResultDao, subjectDao)

    @Provides @Singleton
    fun provideFlashcardRepository(
        flashcardDao: FlashcardDao, quizResultDao: QuizResultDao
    ): FlashcardRepository = FlashcardRepository(flashcardDao, quizResultDao)

    @Provides @Singleton
    fun provideChatRepository(
        chatMessageDao: ChatMessageDao
    ): ChatRepository = ChatRepository(chatMessageDao)
}