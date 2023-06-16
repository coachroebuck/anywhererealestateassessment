package com.sample.simpsonsviewer.api.network

import android.annotation.SuppressLint
import java.security.SecureRandom
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager

class NullHostNameVerifier : HostnameVerifier {
    @SuppressLint("BadHostnameVerifier")
    override fun verify(hostname: String, session: SSLSession): Boolean {
        return true
    }

    companion object {
        fun getSocketFactory(): SSLSocketFactory {
            // Create a trust manager that does not validate certificate chains
            @SuppressLint("TrustAllX509TrustManager")
            val trustAllCerts =
                arrayOf<TrustManager>(NullX509TrustManager())
            val sc: SSLContext = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())
            return sc.socketFactory
        }
    }
}
