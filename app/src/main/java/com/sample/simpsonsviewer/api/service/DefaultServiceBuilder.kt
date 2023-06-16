package com.sample.simpsonsviewer.api.service

import com.sample.simpsonsviewer.api.client.ApiClientBuilder
import com.sample.simpsonsviewer.api.client.OkHttpClientBuilder

class DefaultServiceBuilder(
    private val okHttpClientBuilder: OkHttpClientBuilder,
    private val apiClientBuilder: ApiClientBuilder
) : ServiceBuilder {
    override fun build(): SimpsonsCharactersService? {
        val okHttpClient = okHttpClientBuilder.build()
        val retrofit = apiClientBuilder
            .setClient(okHttpClient)
            .build()
        return retrofit.create(SimpsonsCharactersService::class.java)
    }
}
