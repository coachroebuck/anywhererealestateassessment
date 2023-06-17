package com.sample.simpsonsviewer.api.service

import okhttp3.ResponseBody
import retrofit2.Call

class DefaultSimpsonsCharactersAdapter(
    private val serviceBuilder: ServiceBuilder
    ) : SimpsonsCharactersAdapter {
    override fun start(text: String?): Call<ResponseBody>? {
        val service = serviceBuilder.build()
        return service?.start(text)
    }
}