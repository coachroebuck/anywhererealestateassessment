package com.sample.simpsonsviewer.di

import com.sample.simpsonsviewer.api.client.ApiClientBuilder
import com.sample.simpsonsviewer.api.client.DefaultApiClientBuilder
import com.sample.simpsonsviewer.api.client.DefaultOkHttpClientBuilder
import com.sample.simpsonsviewer.api.client.OkHttpClientBuilder
import com.sample.simpsonsviewer.api.network.AppNetworkManager
import com.sample.simpsonsviewer.api.network.DefaultAppNetworkManager
import com.sample.simpsonsviewer.api.service.DefaultServiceBuilder
import com.sample.simpsonsviewer.api.service.DefaultSimpsonsCharactersAdapter
import com.sample.simpsonsviewer.api.service.ServiceBuilder
import com.sample.simpsonsviewer.api.service.SimpsonsCharactersAdapter
import com.sample.simpsonsviewer.log.DefaultLogAdapter
import com.sample.simpsonsviewer.log.LogAdapter
import com.sample.simpsonsviewer.log.LoggingInterceptor
import com.sample.simpsonsviewer.util.AppCoroutineScope
import com.sample.simpsonsviewer.wrapper.ActivityCompatWrapper
import com.sample.simpsonsviewer.wrapper.ContextCompatWrapper
import com.sample.simpsonsviewer.wrapper.DefaultActivityCompatWrapper
import com.sample.simpsonsviewer.wrapper.DefaultContextCompatWrapper
import com.sample.simpsonsviewer.wrapper.DefaultLaunchActivityWrapper
import com.sample.simpsonsviewer.wrapper.LaunchActivityWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WrappersModule {
    private const val protocol = "https://"
    private const val domain = "api.duckduckgo.com"

    @Singleton
    @Provides
    fun provideLogAdapter(): LogAdapter = DefaultLogAdapter()

    @Singleton
    @Provides
    fun provideLoggingInterceptor(logAdapter: LogAdapter): LoggingInterceptor =
        LoggingInterceptor(logAdapter)

    @Provides
    @Singleton
    fun provideServiceBuilder(
        okHttpClientBuilder: OkHttpClientBuilder,
        apiClientBuilder: ApiClientBuilder,
    ): ServiceBuilder = DefaultServiceBuilder(
        okHttpClientBuilder = okHttpClientBuilder,
        apiClientBuilder = apiClientBuilder,
    )

    @Singleton
    @Provides
    fun provideOkHttpClientBuilder(
        loggingInterceptor: LoggingInterceptor
    ): OkHttpClientBuilder = DefaultOkHttpClientBuilder(loggingInterceptor)

    @Singleton
    @Provides
    fun provideContextCompatWrapper(): ContextCompatWrapper = DefaultContextCompatWrapper()

    @Singleton
    @Provides
    fun provideActivityCompatWrapper(): ActivityCompatWrapper = DefaultActivityCompatWrapper()

    @Singleton
    @Provides
    fun provideApiClientBuilder(): ApiClientBuilder =
        DefaultApiClientBuilder(protocol)
            .setDomain(domain)

    @Singleton
    @Provides
    fun provideSimpsonsCharactersAdapter(serviceBuilder: ServiceBuilder): SimpsonsCharactersAdapter =
        DefaultSimpsonsCharactersAdapter(serviceBuilder)

    @Singleton
    @Provides
    fun provideAppNetworkManager(): AppNetworkManager = DefaultAppNetworkManager()

    @Singleton
    @Provides
    fun provideLaunchActivityWrapper(): LaunchActivityWrapper = DefaultLaunchActivityWrapper()

    @Singleton
    @Provides
    fun provideCoroutineScope(): CoroutineScope = AppCoroutineScope()
}