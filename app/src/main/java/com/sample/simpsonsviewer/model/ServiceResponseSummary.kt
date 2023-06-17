package com.sample.simpsonsviewer.model

data class ServiceResponseSummary(
    val title: String,
    val details: String,
    val icon: String? = null,
    val url: String? = null,
)
