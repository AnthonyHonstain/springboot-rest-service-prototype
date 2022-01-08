package com.example.restservice

import io.micrometer.core.instrument.MeterRegistry

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


//@Configuration(proxyBeanMethods = false)
//class MetricsConfiguration {
//
//    // Trying to follow https://micrometer.io/docs/registry/graphite#_hierarchical_name_mapping
//    // This also helped me https://stackoverflow.com/questions/49872765/add-prefix-to-graphite-micrometer-event-in-spring-boot
//    // More graphite specific shit https://github.com/micrometer-metrics/micrometer/issues/348
//    @Bean
//    fun metricsCommonTags(): MeterRegistryCustomizer<MeterRegistry> {
//        return MeterRegistryCustomizer { registry: MeterRegistry ->
//            registry.config().commonTags("app", "Anthony-Test")
//        }
//    }
//}