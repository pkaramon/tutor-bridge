package org.tutorBridge;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.tutorBridge.entities.*;
import org.tutorBridge.service.StudentService;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        StudentService studentService = new StudentService();

        Student s = new Student(
                "John",
                "Doe",
                "1234567890",
                "johndoe321321321@gmail.com",
                "password123",
                "High School",
                LocalDate.of(2000, 1,1)
        );

        studentService.registerStudent(s);


    }
}
