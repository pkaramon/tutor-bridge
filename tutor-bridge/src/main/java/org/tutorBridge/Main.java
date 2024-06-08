package org.tutorBridge;

import org.tutorBridge.dao.StudentDao;
import org.tutorBridge.entities.Student;
import org.tutorBridge.entities.StudentLevel;
import org.tutorBridge.service.StudentService;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        StudentService studentService = new StudentService();

        Student s = new Student(
                "John",
                "DoeDoe",
                "123123123",
                "johndoedoe@gmail.com",
                "password",
                StudentLevel.UNIVERSITY,
                LocalDate.of(1999, 1, 1)
        );

        studentService.registerStudent(s);



    }
}
