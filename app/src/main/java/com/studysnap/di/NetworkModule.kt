package com.studysnap.di

import com.studysnap.data.api.OpenRouterClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOpenRouterClient(): OpenRouterClient = OpenRouterClient()
}