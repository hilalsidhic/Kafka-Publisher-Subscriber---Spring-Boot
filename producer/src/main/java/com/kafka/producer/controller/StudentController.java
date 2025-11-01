package com.kafka.producer.controller;

import com.kafka.producer.entities.Student;
import com.kafka.producer.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping("/publish")
    public void publishStudentController(@RequestBody Student s){
        studentService.publishStudent(s);
        return;
    }

}
