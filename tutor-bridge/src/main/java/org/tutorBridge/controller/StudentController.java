package org.tutorBridge.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tutorBridge.dto.NewReservationDTO;
import org.tutorBridge.dto.NewReservationsDTO;
import org.tutorBridge.dto.StudentRegisterDTO;
import org.tutorBridge.dto.StudentUpdateDTO;
import org.tutorBridge.entities.Student;
import org.tutorBridge.services.StudentService;

import java.util.Collections;
import java.util.List;
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

    @PostMapping("/update")
    public Map<String, String> updateStudent(@Valid @RequestBody StudentUpdateDTO studentData,
                                             Authentication authentication) {
        String email = authentication.getName();
        studentService.updateStudentInfo(email, studentData);
        return Collections.singletonMap("message", "Student information updated successfully");
    }

    @PostMapping("/reservation")
    public Map<String, String> makeReservations(@Valid @RequestBody NewReservationsDTO reservations,
                                                Authentication authentication) {
        String email = authentication.getName();
        studentService.makeReservations(email, reservations.getReservations());
        return Collections.singletonMap("message", "Reservations made successfully");
    }

}
