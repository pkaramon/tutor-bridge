package org.tutorBridge.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.tutorBridge.dto.StudentRegisterDTO;
import org.tutorBridge.entities.Student;
import org.tutorBridge.services.StudentService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    public Map<String, String> registerStudent(@Valid @RequestBody StudentRegisterDTO studentData) {
        Student student = new Student(
                studentData.getFirstName(),
                studentData.getLastName(),
                studentData.getPhone(),
                studentData.getEmail(),
                studentData.getPassword(),
                studentData.getLevel(),
                studentData.getBirthDate()
        );

        studentService.registerStudent(student);
        return Collections.singletonMap("message", "Student registered successfully");
    }
}
