package org.tutorBridge.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.dto.TutorDTO;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.entities.User;
import org.tutorBridge.mapper.TutorMapper;
import org.tutorBridge.services.TutorService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/tutors")
public class TutorController {
    private final TutorService tutorService;
    private final TutorMapper tutorMapper;

    @Autowired
    private TutorDao tutorDao;

    public TutorController(TutorService tutorService, TutorMapper tutorMapper) {
        this.tutorService = tutorService;
        this.tutorMapper = tutorMapper;
    }

    @PostMapping("/register")
    public TutorDTO registerTutor(@Valid @RequestBody TutorDTO tutor) {
        Tutor tutorEntity = tutorMapper.toEntity(tutor);

        System.out.println("##########");
        System.out.println(tutorEntity.getFirstName());
        System.out.println(tutorEntity.getLastName());
        System.out.println(tutorEntity.getEmail());
        System.out.println(tutor.getEmail());
        System.out.println(tutorEntity.getBirthDate());
        System.out.println(tutorEntity.getPhone());
        System.out.println("##########");

        tutorService.registerTutor(tutorEntity);
        return tutorMapper.toDto(tutorEntity);
    }

    @PersistenceContext
    private EntityManager em;

    @GetMapping("/hello")
    public String hello() {
        // find all users
        List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
        return users.stream().map(User::getFirstName).reduce("", (a, b) -> a + " " + b);
    }

}
