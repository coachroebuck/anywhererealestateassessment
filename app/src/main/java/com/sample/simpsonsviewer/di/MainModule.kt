package com.sample.simpsonsviewer.di

import com.sample.simpsonsviewer.api.service.SimpsonsCharactersAdapter
import com.sample.simpsonsviewer.log.LogAdapter
import com.sample.simpsonsviewer.main.DefaultMainInteraction
import com.sample.simpsonsviewer.main.DefaultMainRepository
import com.sample.simpsonsviewer.main.DefaultMainViewModel
import com.sample.simpsonsviewer.main.MainInteraction
import com.sample.simpsonsviewer.main.MainRepository
import com.sample.simpsonsviewer.main.MainViewModel
import com.sample.simpsonsviewer.serialization.AppSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    private const val domain = "https://duckduckgo.com"
    @Provides
    @Singleton
    fun provideMainRepository(
        simpsonsCharactersAdapter: SimpsonsCharactersAdapter,
        logAdapter: LogAdapter,
        appSerializer: AppSerializer,
        coroutineScope: CoroutineScope,
    ): MainRepository = DefaultMainRepository(
        simpsonsCharactersAdapter,
        logAdapter,
        appSerializer,
        coroutineScope
    )

    @Provides
    @Singleton
    fun provideMainInteraction(
        repository: MainRepository,
        coroutineScope: CoroutineScope,
    ): MainInteraction =
        DefaultMainInteraction(repository, coroutineScope, domain)

    @Provides
    @Singleton
    fun provideMainViewModel(
        interaction: MainInteraction,
        coroutineScope: CoroutineScope
    ): MainViewModel =
        DefaultMainViewModel(interaction, coroutineScope)

}