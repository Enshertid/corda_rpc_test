package com.enshertid.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestApiService

fun main(args: Array<String>) {
    runApplication<TestApiService>(*args)
}
