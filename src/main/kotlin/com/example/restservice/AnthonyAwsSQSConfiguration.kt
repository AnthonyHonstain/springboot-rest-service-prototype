package com.example.restservice

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.task.AsyncTaskExecutor
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.*


@Configuration
@EnableSqs
class AnthonyAwsSQSConfiguration {

    @Bean
    @Primary
    fun amazonSQSAsync(): AmazonSQSAsync {
        return AmazonSQSAsyncClientBuilder.standard()
                .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(
                        "http://localhost:4566/000000000000/anthony-queue",
                        "us-west-1"
                ))
                .withCredentials(DefaultAWSCredentialsProviderChain())
                //.withRegion("us-west-1")
                .build()
    }

    @Bean
    fun simpleMessageListenerContainerFactory(amazonSQSAsync: AmazonSQSAsync): SimpleMessageListenerContainerFactory {
        val factory = SimpleMessageListenerContainerFactory()
        factory.setAmazonSqs(amazonSQSAsync)
        factory.setAutoStartup(true)
        factory.setMaxNumberOfMessages(10) // Note - you can't set this beyond 10, it will throw at startup in a shitty way.
        factory.setTaskExecutor(createDefaultTaskExecutor())
        //factory.setTaskExecutor(simpleAsyncTaskExecutor())
        return factory
    }

    fun simpleAsyncTaskExecutor(): SimpleAsyncTaskExecutor {
        val simpleAsyncTaskExecutor = SimpleAsyncTaskExecutor()
        simpleAsyncTaskExecutor.concurrencyLimit = 50
        return simpleAsyncTaskExecutor
    }

    fun createDefaultTaskExecutor(): AsyncTaskExecutor {
        val threadPoolTaskExecutor = ThreadPoolTaskExecutor()
        threadPoolTaskExecutor.setThreadNamePrefix("SQSExecutor - ")
        threadPoolTaskExecutor.corePoolSize = 10
        threadPoolTaskExecutor.maxPoolSize = 10
        threadPoolTaskExecutor.setQueueCapacity(200)
        threadPoolTaskExecutor.afterPropertiesSet()
        return threadPoolTaskExecutor
    }

    @Bean
    fun anthonyMessageListenerExecutor(): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.maxPoolSize = 100
        executor.setQueueCapacity(0)
        executor.setThreadNamePrefix("Anthony_Listener - ")
        executor.setRejectedExecutionHandler(BlockingSubmissionPolicy(3000))
        executor.afterPropertiesSet()
        return executor
    }

    class BlockingSubmissionPolicy(val timeout: Long) : RejectedExecutionHandler {

        override fun rejectedExecution(p0: Runnable, p1: ThreadPoolExecutor) {
            try {
                val queue: BlockingQueue<Runnable> = p1.queue
                if (!queue.offer(p0, timeout, TimeUnit.MILLISECONDS)) {
                    throw RejectedExecutionException("Timeout")
                }
            } catch (e: InterruptedException){
                Thread.currentThread().interrupt()
            }
        }
    }
}