package com.kafka.producer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.producer.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Value("${kafka.consumer.topic.name}")
    private String topic;

    public void publishStudent(Student s){
        try{
            kafkaTemplate.send(topic,1,s.id.toString(),objectMapper.writeValueAsString(s));
            kafkaTemplate.send(topic,0,s.id.toString(),objectMapper.writeValueAsString(s));
            System.out.println("Message Sent");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
