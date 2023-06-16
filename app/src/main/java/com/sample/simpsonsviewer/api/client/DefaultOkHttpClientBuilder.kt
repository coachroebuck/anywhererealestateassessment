package com.sample.simpsonsviewer.api.client

import com.sample.simpsonsviewer.api.network.NullHostNameVerifier
import com.sample.simpsonsviewer.log.LoggingInterceptor
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class DefaultOkHttpClientBuilder(
    private val interceptor: LoggingInterceptor? = null,
) : OkHttpClientBuilder {

    private val timeInSeconds = 30L
    override fun build(): OkHttpClient {
        val trustManagerFactory: TrustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            ("Unexpected default trust managers:"
                    + trustManagers.contentToString())
        }
        val trustManager: X509TrustManager = trustManagers[0] as X509TrustManager

        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

        val builder = OkHttpClient.Builder()
        interceptor?.let {
            builder.addInterceptor(it)
        }
        return builder
            .sslSocketFactory(sslSocketFactory, trustManager)
            .hostnameVerifier(NullHostNameVerifier())
            .connectTimeout(timeInSeconds, TimeUnit.SECONDS)
            .readTimeout(timeInSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeInSeconds, TimeUnit.SECONDS)
            .build()
    }
}