package com.sample.simpsonsviewer.api.client

import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface ApiClientBuilder {
    fun setDomain(value: String): ApiClientBuilder
    fun setClient(value: OkHttpClient): ApiClientBuilder
    fun build(): Retrofit
}
