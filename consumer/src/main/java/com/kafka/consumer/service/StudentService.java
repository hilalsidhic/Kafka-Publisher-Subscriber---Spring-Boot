package com.kafka.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.consumer.entities.Student;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(groupId = "${notification.consumer.group-id}",
            topicPartitions = {
            @TopicPartition(topic = "${kafka.consumer.topic.name}", partitions = {"0","1","2"})
            })
    public void consume(ConsumerRecord<String,String> record){
        try{

            String key = record.key();
            String value = record.value();

            Student student = objectMapper.readValue(value, Student.class);

            System.out.println(student);
            System.out.println("Partition : " + record.partition() + " and Offset : " + record.offset());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
