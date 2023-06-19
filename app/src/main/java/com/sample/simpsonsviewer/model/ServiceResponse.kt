package com.sample.simpsonsviewer.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceResponse (
    @SerialName("Abstract")
    val abstract: String? = null,

    @SerialName("AbstractSource")
    val abstractSource: String? = null,

    @SerialName("AbstractText")
    val abstractText: String? = null,

    @SerialName("AbstractURL")
    val abstractURL: String? = null,

    @SerialName("Answer")
    val answer: String? = null,

    @SerialName("AnswerType")
    val answerType: String? = null,

    @SerialName("Definition")
    val definition: String? = null,

    @SerialName("DefinitionSource")
    val definitionSource: String? = null,

    @SerialName("DefinitionURL")
    val definitionURL: String? = null,

    @SerialName("Entity")
    val entity: String? = null,

    @SerialName("Heading")
    val heading: String? = null,

    @SerialName("Image")
    val image: String? = null,

    @SerialName("ImageHeight")
    val imageHeight: String? = null,

    @SerialName("ImageIsLogo")
    val imageIsLogo: String? = null,

    @SerialName("ImageWidth")
    val imageWidth: String? = null,

    @SerialName("Infobox")
    val infobox: String? = null,

    @SerialName("Redirect")
    val redirect: String? = null,

    @SerialName("RelatedTopics")
    val relatedTopics: List<RelatedTopic> = listOf(),

    @SerialName("Results")
    val results: List<String?> = listOf(),

    @SerialName("Type")
    val type: String? = null,

    val meta: Meta? = null,
)

@Serializable
data class Meta (
    val attribution: String? = null,
    val blockgroup: String? = null,

    @SerialName("created_date")
    val createdDate: String? = null,

    val description: String? = null,
    val designer: String? = null,

    @SerialName("dev_date")
    val devDate: String? = null,

    @SerialName("dev_milestone")
    val devMilestone: String? = null,

    val developer: List<Developer> = listOf(),

    @SerialName("example_query")
    val exampleQuery: String? = null,

    val id: String? = null,

    @SerialName("is_stackexchange")
    val isStackexchange: String? = null,

    @SerialName("js_callback_name")
    val jsCallbackName: String? = null,

    @SerialName("live_date")
    val liveDate: String? = null,

    val maintainer: Maintainer? = null,
    val name: String? = null,

    @SerialName("perl_module")
    val perlModule: String? = null,

    val producer: String? = null,

    @SerialName("production_state")
    val productionState: String? = null,

    val repo: String? = null,

    @SerialName("signal_from")
    val signalFrom: String? = null,

    @SerialName("src_domain")
    val srcDomain: String? = null,

    @SerialName("src_id")
    val srcID: Long? = null,

    @SerialName("src_name")
    val srcName: String? = null,

    @SerialName("src_options")
    val srcOptions: SrcOptions? = null,

    @SerialName("src_url")
    val srcURL: String? = null,

    val status: String? = null,
    val tab: String? = null,
    val topic: List<String> = listOf(),
    val unsafe: Long? = null
)

@Serializable
data class Developer (
    val name: String? = null,
    val type: String? = null,
    val url: String? = null
)

@Serializable
data class Maintainer (
    val github: String? = null
)

@Serializable
data class SrcOptions (
    val directory: String? = null,

    @SerialName("is_fanon")
    val isFanon: Long? = null,

    @SerialName("is_mediawiki")
    val isMediawiki: Long? = null,

    @SerialName("is_wikipedia")
    val isWikipedia: Long? = null,

    val language: String? = null,

    @SerialName("min_abstract_length")
    val minAbstractLength: String? = null,

    @SerialName("skip_abstract")
    val skipAbstract: Long? = null,

    @SerialName("skip_abstract_paren")
    val skipAbstractParen: Long? = null,

    @SerialName("skip_end")
    val skipEnd: String? = null,

    @SerialName("skip_icon")
    val skipIcon: Long? = null,

    @SerialName("skip_image_name")
    val skipImageName: Long? = null,

    @SerialName("skip_qr")
    val skipQr: String? = null,

    @SerialName("source_skip")
    val sourceSkip: String? = null,

    @SerialName("src_info")
    val srcInfo: String? = null
)

@Serializable
data class RelatedTopic (
    @SerialName("FirstURL")
    val firstURL: String? = null,

    @SerialName("Icon")
    val icon: Icon? = null,

    @SerialName("Result")
    val result: String? = null,

    @SerialName("Text")
    val text: String? = null
)

@Serializable
data class Icon (
    @SerialName("Height")
    val height: String? = null,

    @SerialName("URL")
    val url: String? = null,

    @SerialName("Width")
    val width: String? = null
)
