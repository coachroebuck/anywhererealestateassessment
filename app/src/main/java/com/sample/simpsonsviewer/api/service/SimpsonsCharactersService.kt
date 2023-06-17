package com.sample.simpsonsviewer.api.service

import com.sample.simpsonsviewer.BuildConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SimpsonsCharactersService {
    @GET("/?format=json")
    fun start(@Query("q") query: String?): Call<ResponseBody>
}