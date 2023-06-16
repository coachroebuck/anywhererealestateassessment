package com.sample.simpsonsviewer.api.service

interface ServiceBuilder {
    fun build(): SimpsonsCharactersService?
}