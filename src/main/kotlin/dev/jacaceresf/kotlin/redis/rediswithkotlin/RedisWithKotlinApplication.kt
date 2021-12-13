package dev.jacaceresf.kotlin.redis.rediswithkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RedisWithKotlinApplication

fun main(args: Array<String>) {
    runApplication<RedisWithKotlinApplication>(*args)
}
