package com.sample.simpsonsviewer.log

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.asResponseBody
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LoggingInterceptor(private val logAdapter: LogAdapter) : Interceptor {

    private var responseBodyText: String? = null

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()
            .method(original.method, original.body)
        val requestBuffer = Buffer()
        original.body?.writeTo(requestBuffer)
        val requestBodyText = requestBuffer.readUtf8()
        val request = builder.build()
        val df = SimpleDateFormat("yyyy-mm-dd' 'HH:mm:ss", Locale.getDefault())
        val sbRequest = StringBuilder()
        sbRequest.append("Request: \n")
        sbRequest.append("path: " + original.url.toString() + "\n")
        sbRequest.append("method: " + original.method + "\n")
        sbRequest.append("headers: " + original.headers.toString() + "\n")
        sbRequest.append("body: $requestBodyText\n")
        logAdapter.i(sbRequest.toString())

        val response = chain.proceed(request)

        val clone = Response.Builder()
            .protocol(response.protocol)
            .code(response.code)
            .message(response.message)
            .handshake(response.handshake)
            .headers(response.headers)
            .body(cloneResponseBody(response.body))
            .networkResponse(response.networkResponse)
            .cacheResponse(response.cacheResponse)
            .priorResponse(response.priorResponse)
            .sentRequestAtMillis(response.sentRequestAtMillis)
            .receivedResponseAtMillis(response.receivedResponseAtMillis)
            .request(response.request).build()
        val dateSent = Date(clone.sentRequestAtMillis)
        val dateReceived = Date(clone.receivedResponseAtMillis)
        val sb = StringBuilder()
        sb.append("Response: \n")
        sb.append("path: " + original.url.toString() + "\n")
        sb.append("Sent: " + df.format(dateSent) + "\n")
        sb.append("Received: " + df.format(dateReceived) + "\n")
        sb.append("code: " + clone.code.toString() + "\n")
        sb.append("message: " + clone.message + "\n")
        sb.append("headers: " + clone.headers.toString() + "\n")

        val text = responseBodyText //getTextFromResponseBody(clone.body())
        text ?.let {
            sb.append("body: $it\n")
        }

        logAdapter.i(sb.toString())

        return response
    }

    private fun cloneResponseBody(original: ResponseBody?): ResponseBody? {
        val bufferClone = original?.source()?.buffer?.clone()
        var copy: ResponseBody? = null
        original?.contentLength()?.let {
            bufferClone?.let { it1 ->
                original.contentType()?.let { it2 ->
                    copy = it1.asResponseBody(it2, it)
                }
            }
        }
        responseBodyText = getTextFromResponseBody(original)
        return copy
    }

    private fun getTextFromResponseBody(original: ResponseBody?): String? {
        original?.let {
            val source = it.source()
            source.request(Long.MAX_VALUE)
            return source.buffer.clone().readString(Charset.forName("UTF-8"))
        }

        return null
    }
}
