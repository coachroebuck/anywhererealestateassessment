package com.sample.simpsonsviewer.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceResponse (
    @SerialName("Abstract")
    val abstract: String,

    @SerialName("AbstractSource")
    val abstractSource: String,

    @SerialName("AbstractText")
    val abstractText: String,

    @SerialName("AbstractURL")
    val abstractURL: String,

    @SerialName("Answer")
    val answer: String,

    @SerialName("AnswerType")
    val answerType: String,

    @SerialName("Definition")
    val definition: String,

    @SerialName("DefinitionSource")
    val definitionSource: String,

    @SerialName("DefinitionURL")
    val definitionURL: String,

    @SerialName("Entity")
    val entity: String,

    @SerialName("Heading")
    val heading: String,

    @SerialName("Image")
    val image: String,

    @SerialName("ImageHeight")
    val imageHeight: Long,

    @SerialName("ImageIsLogo")
    val imageIsLogo: Long,

    @SerialName("ImageWidth")
    val imageWidth: Long,

    @SerialName("Infobox")
    val infobox: String,

    @SerialName("Redirect")
    val redirect: String,

    @SerialName("RelatedTopics")
    val relatedTopics: List<RelatedTopic>,

    @SerialName("Results")
    val results: List<String?>,

    @SerialName("Type")
    val type: String,

    val meta: Meta
)

@Serializable
data class Meta (
    val attribution: String? = null,
    val blockgroup: String? = null,

    @SerialName("created_date")
    val createdDate: String? = null,

    val description: String,
    val designer: String? = null,

    @SerialName("dev_date")
    val devDate: String? = null,

    @SerialName("dev_milestone")
    val devMilestone: String,

    val developer: List<Developer>,

    @SerialName("example_query")
    val exampleQuery: String,

    val id: String,

    @SerialName("is_stackexchange")
    val isStackexchange: String? = null,

    @SerialName("js_callback_name")
    val jsCallbackName: String,

    @SerialName("live_date")
    val liveDate: String? = null,

    val maintainer: Maintainer,
    val name: String,

    @SerialName("perl_module")
    val perlModule: String,

    val producer: String? = null,

    @SerialName("production_state")
    val productionState: String,

    val repo: String,

    @SerialName("signal_from")
    val signalFrom: String,

    @SerialName("src_domain")
    val srcDomain: String,

    @SerialName("src_id")
    val srcID: Long,

    @SerialName("src_name")
    val srcName: String,

    @SerialName("src_options")
    val srcOptions: SrcOptions,

    @SerialName("src_url")
    val srcURL: String? = null,

    val status: String,
    val tab: String,
    val topic: List<String>,
    val unsafe: Long
)

@Serializable
data class Developer (
    val name: String,
    val type: String,
    val url: String
)

@Serializable
data class Maintainer (
    val github: String
)

@Serializable
data class SrcOptions (
    val directory: String,

    @SerialName("is_fanon")
    val isFanon: Long,

    @SerialName("is_mediawiki")
    val isMediawiki: Long,

    @SerialName("is_wikipedia")
    val isWikipedia: Long,

    val language: String,

    @SerialName("min_abstract_length")
    val minAbstractLength: String,

    @SerialName("skip_abstract")
    val skipAbstract: Long,

    @SerialName("skip_abstract_paren")
    val skipAbstractParen: Long,

    @SerialName("skip_end")
    val skipEnd: String,

    @SerialName("skip_icon")
    val skipIcon: Long,

    @SerialName("skip_image_name")
    val skipImageName: Long,

    @SerialName("skip_qr")
    val skipQr: String,

    @SerialName("source_skip")
    val sourceSkip: String,

    @SerialName("src_info")
    val srcInfo: String
)

@Serializable
data class RelatedTopic (
    @SerialName("FirstURL")
    val firstURL: String,

    @SerialName("Icon")
    val icon: Icon,

    @SerialName("Result")
    val result: String,

    @SerialName("Text")
    val text: String
)

@Serializable
data class Icon (
    @SerialName("Height")
    val height: String,

    @SerialName("URL")
    val url: String,

    @SerialName("Width")
    val width: String
)
