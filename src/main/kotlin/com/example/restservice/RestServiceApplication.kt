package com.example.restservice

import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class RestServiceApplication

@Bean
fun threadMetrics(): JvmThreadMetrics {
	return JvmThreadMetrics()
}

fun main(args: Array<String>) {
	runApplication<RestServiceApplication>(*args)
}
