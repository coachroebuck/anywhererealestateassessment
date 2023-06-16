package com.sample.simpsonsviewer.di

import com.sample.simpsonsviewer.serialization.AppSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SerializationModule {
    @Provides
    @Singleton
    fun provideAppSerializer(): AppSerializer = AppSerializer()
}