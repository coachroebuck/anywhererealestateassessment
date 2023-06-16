package com.sample.simpsonsviewer.serialization

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AppSerializer {
    val json = Json {
        encodeDefaults = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
        prettyPrint = true
        useArrayPolymorphism = true
        ignoreUnknownKeys = true
    }

    inline fun <reified T>String.toObject(): T? =
        json.decodeFromString(this) as T

    inline fun <reified T>T.toJson(): String {
        return json.encodeToString(this)
    }
}
