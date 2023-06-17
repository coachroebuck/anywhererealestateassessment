package com.sample.simpsonsviewer.model

import kotlinx.serialization.Serializable

@Serializable
data class ServiceResponseSummary(
    val title: String,
    val details: String,
    val icon: String? = null,
    val url: String? = null,
) : java.io.Serializable
