package org.tutorBridge.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.tutorBridge.dto.*;
import org.tutorBridge.entities.Student;
import org.tutorBridge.services.PlanService;
import org.tutorBridge.services.ReservationService;
import org.tutorBridge.services.StudentService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;
    private final PlanService planService;
    private final ReservationService reservationService;

    public StudentController(StudentService studentService,
                             PlanService planService,
                             ReservationService reservationService) {
        this.studentService = studentService;
        this.planService = planService;
        this.reservationService = reservationService;
    }

    @PostMapping("/register")
    public Map<String, String> registerStudent(@Valid @RequestBody StudentRegisterDTO studentData) {
        studentService.registerStudent(studentData);
        return Collections.singletonMap("message", "Student registered successfully");
    }

    @PutMapping("/account")
    public StudentUpdateDTO updateStudentInfo(@Valid @RequestBody StudentUpdateDTO studentData,
                                              Authentication authentication) {
        String email = authentication.getName();
        return studentService.updateStudentInfo(studentService.fromEmail(email), studentData);
    }

    @GetMapping("/account")
    public StudentUpdateDTO getStudentInfo(Authentication authentication) {
        String email = authentication.getName();
        return studentService.getStudentInfo(studentService.fromEmail(email));
    }

    @PostMapping("/reservation")
    public PlanResponseDTO makeReservations(@Valid @RequestBody NewReservationsDTO reservations,
                                            Authentication authentication) {
        String email = authentication.getName();
        var student = studentService.fromEmail(email);
        reservationService.makeReservations(student, reservations.getReservations());
        return planService.getPlanForStudent(student, TimeFrameDTO.fillInEmptyFields(null));
    }

    @PostMapping("/reservation/{id}/cancel")
    public PlanResponseDTO cancelReservation(@PathVariable(name = "id") Long id, Authentication authentication) {
        String email = authentication.getName();
        Student student = studentService.fromEmail(email);
        reservationService.cancelReservation(student, id);
        return planService.getPlanForStudent(student, TimeFrameDTO.fillInEmptyFields(null));
    }

    @GetMapping("/reservation")
    public PlanResponseDTO getPlan(
            @RequestBody(required = false) TimeFrameDTO timeframe,
            Authentication authentication) {
        String email = authentication.getName();
        return planService.getPlanForStudent(studentService.fromEmail(email), timeframe);
    }


}
