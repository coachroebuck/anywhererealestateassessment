package com.sample.simpsonsviewer.api.service

import okhttp3.ResponseBody
import retrofit2.Call

interface SimpsonsCharactersAdapter {
    fun start(): Call<ResponseBody>?
}