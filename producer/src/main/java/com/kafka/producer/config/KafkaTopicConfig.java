package com.kafka.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.consumer.topic.name}")
    private String topicName;

    @Value("${kafka.consumer.topic.partitions}")
    private int partitions;

    @Value("${kafka.consumer.topic.replicas}")
    private int replicas;


    @Bean
    public NewTopic kafkaTopic(){
        return TopicBuilder.name(topicName)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }
}
