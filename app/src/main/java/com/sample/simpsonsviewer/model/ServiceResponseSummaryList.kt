package com.sample.simpsonsviewer.model

import kotlinx.serialization.Serializable

@Serializable
data class ServiceResponseSummaryList(
    val list: List<ServiceResponseSummary>
) : java.io.Serializable
