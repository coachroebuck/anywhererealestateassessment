package com.sample.simpsonsviewer.di

import com.sample.simpsonsviewer.log.LogAdapter
import com.sample.simpsonsviewer.main.DefaultMainRepository
import com.sample.simpsonsviewer.main.DefaultMainViewModel
import com.sample.simpsonsviewer.main.MainRepository
import com.sample.simpsonsviewer.main.MainViewModel
import com.sample.simpsonsviewer.serialization.AppSerializer
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
    fun provideMainRepository(
        logAdapter: LogAdapter,
        appSerializer: AppSerializer,
    ): MainRepository = DefaultMainRepository(logAdapter, appSerializer)

    @Provides
    @Singleton
    fun provideMainViewModel(repository: MainRepository): MainViewModel =
        DefaultMainViewModel(repository)

}