package org.tutorBridge.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.dto.TutorRegisterDTO;
import org.tutorBridge.dto.TutorSpecializationDTO;
import org.tutorBridge.entities.Specialization;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.entities.User;
import org.tutorBridge.services.TutorService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tutors")
public class TutorController {
    private final TutorService tutorService;
    @PersistenceContext
    private EntityManager em;

    public TutorController(TutorService tutorService, TutorDao tutorDao) {
        this.tutorService = tutorService;
    }

    @PostMapping("/register")
    public Map<String, String> registerTutor(@Valid @RequestBody TutorRegisterDTO tutorData) {
        Tutor tutor = new Tutor(
                tutorData.getFirstName(),
                tutorData.getLastName(),
                tutorData.getPhone(),
                tutorData.getEmail(),
                tutorData.getPassword(),
                tutorData.getBio(),
                tutorData.getBirthDate()
        );
        tutor.setSpecializations(
                tutorData.getSpecializations().stream()
                        .map(Specialization::new)
                        .collect(Collectors.toSet())
        );
        tutorService.registerTutor(tutor);
        return Collections.singletonMap("message", "Tutor registered successfully");
    }

    @GetMapping("/hello")
    public String hello() {
        // find all users
        List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
        return users.stream().map(User::getFirstName).reduce("", (a, b) -> a + " " + b);
    }

    @PostMapping("/specializations")
    public Map<String, String> updateSpecializations(@Valid @RequestBody TutorSpecializationDTO tutorSpecializationDTO,
                                                     Authentication authentication) {
        String email = authentication.getName();
        tutorService.updateTutorSpecializations(email, tutorSpecializationDTO.getSpecializations());
        return Collections.singletonMap("message", "Specializations updated successfully");
    }

    @GetMapping("/specializations")
    public TutorSpecializationDTO getTutorSpecializations(Authentication authentication) {
        String email = authentication.getName();
        Set<String> specializations = tutorService.getSpecializations(email).stream()
                .map(Specialization::getName)
                .collect(Collectors.toSet());
        return new TutorSpecializationDTO(specializations);
    }


}
