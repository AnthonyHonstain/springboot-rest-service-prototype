# Getting Started

This was my first Spring Boot service in a long time, using this project to learn and experiment with Spring Boot and the ecosystem.

Critical components:
* REST controller - used for experimenting with metrics gathering and the default threading model for 
* Amazon SQS dependency - significant experimentation done around consuming from and SQS queue.
* ActiveMQ dependency - primarily configured as a publisher to an ActiveMQ queue.

## Testing Locally
* Can use this Postman example to call the exposed endpoint
  * ![](documentation/Postman_2022-01-08%2013-32-43.png)

* ActiveMQ
  * http://localhost:8161/admin/queues.jsp
  * ![](documentation/ActiveMQ%202022-01-08%2013-42-04.png)

* Jupyter to facilitate message generate with some volume
  * ![](documentation/Jupyter%202022-01-08%2013-44-38.png)

* Docker Compose
  * Used this docker-compose to stand up both SQS and an ActiveMQ instance
```yaml
version: "3.8"

services:
  localstack:
    # https://github.com/localstack/localstack#localstack-cloud-developer-tools
    # https://onexlab-io.medium.com/docker-compose-localstack-fadee1e88a49
    # Considering this examples for SpringBoot
    # https://www.netsurfingzone.com/aws/spring-boot-aws-sqs-listener-example/
    # https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#application-properties.server
    # https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.properties-and-configuration.discover-build-in-options-for-external-properties
    # https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.webserver.configure
    # https://spring.io/guides/gs/rest-service/
    container_name: "${LOCALSTACK_DOCKER_NAME-localstack_main}"
    image: localstack/localstack
    network_mode: bridge
    ports:
      - "127.0.0.1:53:53"                # only required for Pro
      - "127.0.0.1:53:53/udp"            # only required for Pro
      - "127.0.0.1:443:443"              # only required for Pro
      - "127.0.0.1:4510-4530:4510-4530"  # only required for Pro
      - "127.0.0.1:4566:4566"
      - "127.0.0.1:4571:4571"
    environment:
      - SERVICES=sqs,activemq
      - DEBUG=${DEBUG- }
      - DATA_DIR=${DATA_DIR- }
      - LAMBDA_EXECUTOR=${LAMBDA_EXECUTOR- }
      - LOCALSTACK_API_KEY=${LOCALSTACK_API_KEY- }  # only required for Pro
      - HOST_TMP_FOLDER=${TMPDIR:-/tmp/}localstack
      - DOCKER_HOST=unix:///var/run/docker.sock
    volumes:
      - "${TMPDIR:-/tmp}/localstack:/tmp/localstack"
      - "/var/run/docker.sock:/var/run/docker.sock"

  activemq:
    # https://hub.docker.com/r/rmohr/activemq
    # REFERENCES
    # This isn't even a thing anymore, its REACTIVE + ActiveMQ
    #              https://github.com/dnvriend/reactive-activemq/blob/master/docker-compose.yml
    # https://hub.docker.com/r/rmohr/activemq

    image: rmohr/activemq:5.15.9
    container_name: activemq
    environment:
      - "TZ=UTC"
    volumes:
      - "./activemq/activemq.xml:/opt/activemq/conf/activemq.xml"
      #- "./activemq/log4j.properties:/opt/activemq/conf/log4j.properties"
    ports:
      - "61616:61616" # broker (admin:adminactivemq)(amq:amq)
      - "8161:8161"   # web    http://boot2docker:8161/admin (admin:admin)

```

* Inspecting SQS
  * http://localhost:4566/health
  * Getting the status of the SQS queue
```text
export DEFAULT_REGION=us-west-1

awslocal sqs create-queue --queue-name anthony-queue --region us-west-1
{
    "QueueUrl": "http://localhost:4566/000000000000/anthony-queue"
}


awslocal sqs send-message --queue-url "http://localhost:4566/000000000000/anthony-queue" --message-body "test-message-01" --region us-west-1
{
    "MD5OfMessageBody": "add1d74f5bf5480b77787cf0ca91f95a",
    "MessageId": "881e0e16-b5bf-d520-1123-dbe16a49185a"
}

awslocal sqs list-queues --region us-west-1
{
    "QueueUrls": [
        "http://localhost:4566/000000000000/anthony-queue"
    ]
}

awslocal sqs get-queue-attributes --queue-url "http://localhost:4566/000000000000/anthony-queue" --attribute-names All
{
    "Attributes": {
        "ApproximateNumberOfMessages": "1590",
        "ApproximateNumberOfMessagesDelayed": "0",
        "ApproximateNumberOfMessagesNotVisible": "10",
        "CreatedTimestamp": "1641155117.114242",
        "DelaySeconds": "0",
        "LastModifiedTimestamp": "1641155117.114242",
        "MaximumMessageSize": "262144",
        "MessageRetentionPeriod": "345600",
        "QueueArn": "arn:aws:sqs:us-west-1:000000000000:anthony-queue",
        "ReceiveMessageWaitTimeSeconds": "0",
        "VisibilityTimeout": "60"
    }
}
```


# Documentation for this Spring Boot Service

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.10/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.10/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.5.4/reference/htmlsingle/#boot-features-developing-web-applications)
* [Graphite](https://docs.spring.io/spring-boot/docs/2.5.4/reference/html/production-ready-features.html#production-ready-metrics-export-graphite)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

