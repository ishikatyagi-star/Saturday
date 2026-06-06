package com.studysnap.di

import android.content.Context
import com.studysnap.ml.LlmManager
import com.studysnap.ml.OcrManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MlModule {

    @Provides
    @Singleton
    fun provideOcrManager(@ApplicationContext context: Context): OcrManager = OcrManager(context)

    @Provides
    @Singleton
    fun provideLlmManager(@ApplicationContext context: Context): LlmManager = LlmManager(context)
}