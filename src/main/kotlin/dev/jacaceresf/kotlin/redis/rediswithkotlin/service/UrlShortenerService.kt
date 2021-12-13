package dev.jacaceresf.kotlin.redis.rediswithkotlin.service

import dev.jacaceresf.kotlin.redis.rediswithkotlin.models.Url
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.commons.codec.digest.MurmurHash2
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class UrlShortenerService(val redisTemplate: ReactiveStringRedisTemplate) {

    suspend fun saveUrl(url: String): Url {
        val hash = MurmurHash2.hash32(url).toString(radix = 16)

        val shortedUrl = Url(url = url, shortUrl = hash, created = LocalDateTime.now(), counter = 0)
        val json = Json.encodeToString(shortedUrl)
        redisTemplate.opsForValue().set(hash, json).awaitFirst()

        return shortedUrl
    }

    suspend fun getUrl(hash: String): String {

        val shortUrl = redisTemplate.opsForValue().get(hash).awaitSingle()

        val url = Json.decodeFromString<Url>(shortUrl)

        val updated = Url(
            url = url.url,
            shortUrl = url.shortUrl,
            created = url.created,
            counter = url.counter + 1
        )

        redisTemplate.opsForValue().set(hash, Json.encodeToString(updated)).awaitSingle()

        return url.url
    }

    suspend fun getAllUrl(): Collection<Url> {
        //get all keys
        val keys = redisTemplate.keys("*")

        ///convert to list
        val keysList = keys.collectList().awaitSingle().dropLast(1)

        val response = mutableListOf<Url>()
        for (key in keysList) {
            val shortUrl = redisTemplate.opsForValue().get(key).awaitSingle()
            response.add(Json.decodeFromString<Url>(shortUrl))
        }
        return response
    }
}