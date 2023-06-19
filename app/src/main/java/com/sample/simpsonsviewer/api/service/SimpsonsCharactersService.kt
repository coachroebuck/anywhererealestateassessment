package com.sample.simpsonsviewer.api.service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SimpsonsCharactersService {
    @GET("/")
    fun start(
        @Query("q") query: String?,
        @Query("format") format: String? = "json"
    ): Call<ResponseBody>
}