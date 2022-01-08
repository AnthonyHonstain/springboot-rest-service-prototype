package com.example.restservice

import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.aws.messaging.listener.Acknowledgment
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener
import org.springframework.core.task.AsyncTaskExecutor
import org.springframework.jms.core.JmsTemplate
import org.springframework.messaging.handler.annotation.Header
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

@Component
class SimpleSQSListener {

    @Autowired
    val jmsTemplate: JmsTemplate? = null

    //@SqsListener(value = ["anthony-queue"], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    ////@Async
    ////@SqsListener(value = ["anthony-queue"], deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    //fun processmessage(
    //        @Header("MessageId") messageId: String,
    //        message: String,
    //        //acknowledgment: Acknowledgment,
    //) {
    //
    //    val date = Date()
    //    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    //    val displayTime: String = format.format(date)
    //    //Thread.sleep(5000)
    //
    //    println("$displayTime SQS consumer MessageID:$messageId '$message'")
    //
    //    //jmsTemplate?.convertAndSend("anthony-test", message)
    //
    //    // Manually Delete - use this with the SqsMessageDeletionPolicy.NEVER in the annotation
    //    //acknowledgment.acknowledge().get()
    //}

    @Autowired
    val anthonyMessageListenerExecutor: AsyncTaskExecutor? = null

    @SqsListener(value = ["anthony-queue"], deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    fun processmessage(
            @Header("MessageId") messageId: String,
            message: String,
            acknowledgment: Acknowledgment,
    ) {

        anthonyMessageListenerExecutor?.submit {
            try {
                val date = Date()
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val displayTime: String = format.format(date)
                //Thread.sleep(5000)

                println("$displayTime SQS consumer MessageID:$messageId '$message'")

                acknowledgment.acknowledge()
            } catch (e: Exception) {

            }
        }

        //jmsTemplate?.convertAndSend("anthony-test", message)

        // Manually Delete - use this with the SqsMessageDeletionPolicy.NEVER in the annotation
        //acknowledgment.acknowledge().get()
    }
}
