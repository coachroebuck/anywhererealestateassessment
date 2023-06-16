package com.sample.simpsonsviewer.api.client

import okhttp3.OkHttpClient

interface OkHttpClientBuilder {
    fun build(): OkHttpClient
}