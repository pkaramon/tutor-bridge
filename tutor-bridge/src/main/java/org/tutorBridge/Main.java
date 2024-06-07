package org.tutorBridge;

import org.tutorBridge.entities.Tutor;
import org.tutorBridge.service.TutorService;
import org.tutorBridge.validation.ValidationException;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
//        StudentService studentService = new StudentService();
//
//        Student s = new Student(
//                "John",
//                "Doe",
//                "1234567890",
//                "johndoe321321321@gmail.com",
//                "password123",
//                "High School",
//                LocalDate.of(2000, 1, 1)
//        );
//
//        studentService.registerStudent(s);


        TutorService tutorService = new TutorService();
        Tutor t = new Tutor(
                "Jane",
                "Smith",
                "0987654321",
                "easdfasf@asdfas",
                "password123",
                "bio",
                LocalDate.of(2000, 1, 1));

        try {

            tutorService.registerTutor(t);
        } catch(ValidationException e) {
            System.out.println(e.getMessages());
        }



    }
}
