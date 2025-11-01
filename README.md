# Spring Boot Kafka Producer/Consumer Demo

This is a simple Spring Boot project that demonstrates a complete, working example of an Apache Kafka producer and consumer using a multi-module Gradle setup.

The application is structured as two modules:
- **producer/**: Spring Boot Kafka producer exposing REST endpoints.
- **consumer/**: Spring Boot Kafka consumer with multiple listener examples.

The application is configured to:
1.  **Produce** messages to a Kafka topic (via a REST API endpoint in the producer module).
2.  **Consume** messages from a Kafka topic using `@KafkaListener` in the consumer module.
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
* **Build Tool:** Gradle (see `producer/` and `consumer/` for individual builds)

## 🏁 Getting Started

Follow these steps to get the entire application running on your local machine.

### Prerequisites

* **Docker & Docker Compose:** Required to run the Kafka cluster.
* **JDK 17** (or newer): Required to run the Spring Boot applications.
* **Gradle:** Required to build and run the applications (or use the included `gradlew` wrapper scripts).

### Step 1: Start the Kafka Cluster

First, start the Kafka and Zookeeper containers using the provided Docker Compose file.

```bash
# This will start Zookeeper on port 2181 and Kafka on port 9092
docker-compose up -d
```

### Step 2: Build the Applications

Build both modules using Gradle (from the root directory):

```bash
# On Windows
cd producer && gradlew build && cd ../consumer && gradlew build
# Or using the Gradle wrapper from each module
```

### Step 3: Run the Producer and Consumer

Open two terminals:

**Terminal 1: Start the Consumer**
```bash
cd consumer
./gradlew bootRun
```

**Terminal 2: Start the Producer**
```bash
cd producer
./gradlew bootRun
```

The consumer must be running before you send messages from the producer.

## 🚀 How to Use

You can test the producer and consumer using any API client (like `curl` or Postman).

### 1. Send a Message (Producer)

The producer exposes a REST endpoint to send messages. Example (adjust if your controller uses a different path):

```bash
# Send a message to the 'dummyTopic'
curl -X POST "http://localhost:8080/send?topic=dummyTopic&message=hello-partition-1"
```

*(Note: You may need to adjust the URL and topic name based on your `ProducerController`)*

### 2. Check the Console (Consumer)

Watch the console output of your running consumer application. The `@KafkaListener` that is assigned to that topic's partition will activate and print the message.

```
Received message from partition 1: hello-partition-1
```

## ⚙️ Key Configuration (`application.properties`)

All application settings are in each module's `src/main/resources/application.properties`.

**Producer:** `producer/src/main/resources/application.properties`
**Consumer:** `consumer/src/main/resources/application.properties`

Example configuration (consumer):

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
spring.kafka.consumer.group-id=my-general-group
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.auto-offset-reset=earliest

# ---------------------------------
# Custom Application Properties
# ---------------------------------
kafka.consumer.topic.name=dummyTopic
notification.consumer.group-id=notification-group
```

## 📄 Kafka Listeners (`@KafkaListener` Examples)

This project demonstrates two types of consumers (see `consumer/src/main/java/com/kafka/consumer/service/`):

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

## 🧪 Testing

You can run tests for each module:

```bash
cd producer
./gradlew test

cd ../consumer
./gradlew test
```

---

For any issues, check the logs in each module or review the configuration files. Make sure Kafka and Zookeeper are running before starting the applications.
