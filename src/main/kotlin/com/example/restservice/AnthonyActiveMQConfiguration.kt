package com.example.restservice

import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.core.JmsTemplate
import javax.jms.ConnectionFactory

@Configuration

class AnthonyActiveMQConfiguration {

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val activeMQConnectionFactory = ActiveMQConnectionFactory()
        activeMQConnectionFactory.brokerURL = "tcp://localhost:61616"
        return activeMQConnectionFactory
    }

    @Bean
    fun jmsTemplate(): JmsTemplate {
        val jmsTemplate = JmsTemplate()
        jmsTemplate.connectionFactory = connectionFactory()
        return jmsTemplate
    }
}