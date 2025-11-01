# Spring Boot Kafka Producer/Consumer Demo

This is a simple Spring Boot project that demonstrates a complete, working example of an Apache Kafka producer and consumer.

The application is configured to:
1.  **Produce** messages to a Kafka topic (e.g., using a REST API endpoint).
2.  **Consume** messages from a Kafka topic using `@KafkaListener`.
3.  Includes an example of a consumer listening to a **specific partition**.

## ✨ Features

* **Kafka Producer:** Uses `KafkaTemplate` to send string messages.
* **Kafka Consumer:** Uses `@KafkaListener` to receive messages.
* **Specific Partition Consumer:** Includes a listener example that is manually assigned to **only one partition**.
* **Docker Environment:** Comes with a `docker-compose.yml` file to instantly start a single-node Kafka and Zookeeper cluster.

## 🛠️ Tech Stack

* **Backend:** Java 17 & Spring Boot 3
* **Messaging:** Spring for Apache Kafka
* **Infrastructure:** Apache Kafka & Zookeeper
* **Containerization:** Docker Compose

## 🏁 Getting Started

Follow these steps to get the entire application running on your local machine.

### Prerequisites

* **Docker & Docker Compose:** Required to run the Kafka cluster.
* **JDK 17** (or newer): Required to run the Spring Boot application.
* **Maven:** Required to build and run the application.

### Step 1: Start the Kafka Cluster

First, start the Kafka and Zookeeper containers using the provided Docker Compose file.

```bash
# This will start Zookeeper on port 2181 and Kafka on port 9092
docker-compose up -d
```

### Step 2: Run the Spring Boot Application

Once the Kafka broker is running, you can start the Spring Boot application.

```bash
# This will build the project and start the server
mvn spring-boot:run
```

The application will start and the `@KafkaListener` will connect to the Kafka broker.

## 🚀 How to Use

You can test the producer and consumer using any API client (like `curl` or Postman).

### 1. Send a Message (Producer)

This project likely exposes a simple REST endpoint to send messages. (Assuming an endpoint like `/send`):

```bash
# Send a message to the 'dummyTopic'
curl -X POST "http://localhost:8080/send?topic=dummyTopic&message=hello-partition-1"
```

*(Note: You may need to adjust the URL and topic name based on your `ProducerController`)*

### 2. Check the Console (Consumer)

Watch the console output of your running Spring Boot application. The `@KafkaListener` that is assigned to that topic's partition will activate and print the message.

```
Received message from partition 1: hello-partition-1
```

## ⚙️ Key Configuration (`application.properties`)

All application settings are in `src/main/resources/application.properties`.

```properties
# ---------------------------------
# Spring Server
# ---------------------------------
server.port=8080

# ---------------------------------
# Kafka Broker
# ---------------------------------
spring.kafka.bootstrap-servers=localhost:9092

# ---------------------------------
# Kafka Producer Properties
# ---------------------------------
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer

# ---------------------------------
# Kafka Consumer Properties
# ---------------------------------
# This is the default group ID for @KafkaListener (can be overridden)
spring.kafka.consumer.group-id=my-general-group
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
# Ensures new groups read from the beginning of the topic
spring.kafka.consumer.auto-offset-reset=earliest

# ---------------------------------
# Custom Application Properties
# ---------------------------------
# Used to inject topic/group names into @KafkaListener
kafka.consumer.topic.name=dummyTopic
notification.consumer.group-id=notification-group
```

## 📄 Kafka Listeners (`@KafkaListener` Examples)

This project demonstrates two types of consumers:

### 1. Standard (Group-Managed) Consumer

This listener joins a consumer group, and Kafka automatically assigns it one or more partitions to read from.

```java
@KafkaListener(topics = "${kafka.consumer.topic.name}", groupId = "my-general-group")
public void listenToAllPartitions(String message) {
    System.out.println("General Listener Received: " + message);
}
```

### 2. Manual Partition Assignment Consumer

This listener **does not** join the consumer group for balancing. It manually connects to and consumes *only* from the specified partitions. This is useful when you need one specific service instance to handle a specific partition.

```java
import org.springframework.kafka.annotation.TopicPartition;

@KafkaListener(groupId = "${notification.consumer.group-id}",
               topicPartitions = {
                   @TopicPartition(topic = "${kafka.consumer.topic.name}", partitions = {"1"})
               })
public void listenToPartitionOne(String message) {
    System.out.println("Received message from partition 1: " + message);
}
```
