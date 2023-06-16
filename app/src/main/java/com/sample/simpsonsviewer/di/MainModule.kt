package com.sample.simpsonsviewer.di

import com.sample.simpsonsviewer.main.DefaultMainViewModel
import com.sample.simpsonsviewer.main.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideMainViewModel(): MainViewModel = DefaultMainViewModel()

}