package com.studysnap.di

import android.content.Context
import androidx.room.Room
import com.studysnap.data.db.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StudySnapDatabase {
        return Room.databaseBuilder(context, StudySnapDatabase::class.java, "studysnap.db").build()
    }

    @Provides fun provideSubjectDao(db: StudySnapDatabase): SubjectDao = db.subjectDao()
    @Provides fun provideConceptDao(db: StudySnapDatabase): ConceptDao = db.conceptDao()
    @Provides fun provideQuizResultDao(db: StudySnapDatabase): QuizResultDao = db.quizResultDao()
    @Provides fun provideFlashcardDao(db: StudySnapDatabase): FlashcardDao = db.flashcardDao()
    @Provides fun provideChatMessageDao(db: StudySnapDatabase): ChatMessageDao = db.chatMessageDao()
}