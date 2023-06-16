package com.sample.simpsonsviewer.api.client

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
class DefaultApiClientBuilder(private val protocol: String) : ApiClientBuilder {
    private var domain: String = ""
    private var client: OkHttpClient = OkHttpClient()

    override fun setDomain(value: String): ApiClientBuilder {
        domain = value
        return this
    }

    override fun setClient(value: OkHttpClient): ApiClientBuilder {
        client = value
        return this
    }

    override fun build(): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("$protocol$domain")
            .client(client)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }
}
