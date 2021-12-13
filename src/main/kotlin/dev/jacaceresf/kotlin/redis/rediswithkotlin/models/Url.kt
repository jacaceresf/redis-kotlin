package dev.jacaceresf.kotlin.redis.rediswithkotlin.models

import dev.jacaceresf.kotlin.redis.rediswithkotlin.helpers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Serializable
data class Url(
    val url: String,
    var shortUrl: String,
    val counter: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val created: LocalDateTime
)

data class UrlDto(val url: String)
