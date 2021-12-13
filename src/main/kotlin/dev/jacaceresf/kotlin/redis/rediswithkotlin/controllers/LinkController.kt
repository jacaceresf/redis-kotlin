package dev.jacaceresf.kotlin.redis.rediswithkotlin.controllers

import dev.jacaceresf.kotlin.redis.rediswithkotlin.models.UrlDto
import dev.jacaceresf.kotlin.redis.rediswithkotlin.service.UrlShortenerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/links")
class LinkController(private val urlShortenerService: UrlShortenerService) {

    @PostMapping
    suspend fun saveUrl(@RequestBody url: UrlDto) = urlShortenerService.saveUrl(url.url)


    @GetMapping("/{hash}")
    suspend fun getUrl(serverHttpResponse: ServerHttpResponse, @PathVariable("hash") hash: String) =
        ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
            .location(URI.create(urlShortenerService.getUrl(hash)))
            .build<Unit>()


    @GetMapping
    suspend fun getUrls() = urlShortenerService.getAllUrl()

}