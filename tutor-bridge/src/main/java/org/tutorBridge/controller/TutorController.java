package org.tutorBridge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.services.TutorService;

@RestController
@RequestMapping("/api/tutors")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @PostMapping("/register")
    public Tutor registerTutor(@RequestBody Tutor tutor) {
        tutorService.registerTutor(tutor);
        return tutor;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    public record Point(int x, int y) { }

}
