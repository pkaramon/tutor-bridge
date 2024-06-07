package org.tutorBridge.service;

import org.tutorBridge.dao.StudentDao;
import org.tutorBridge.dao.UserDao;
import org.tutorBridge.entities.Student;
import org.tutorBridge.security.PasswordManager;
import org.tutorBridge.validation.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class StudentService {
    private final StudentDao studentDao = new StudentDao();
    private final UserDao userDao = new UserDao();
    private final Validator validator;

    public StudentService() {
        var factory= Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public void registerStudent(Student student) {
        List<String> violations = new ArrayList<>();
        boolean userWithSameEmailExists = userDao.findByEmail(student.getEmail()).isPresent();
        if(userWithSameEmailExists) {
            violations.add("User with the same email already exists");
        }
        violations.addAll(validator.validate(student).stream().map(ConstraintViolation::getMessage).toList());
        if(!violations.isEmpty()) {
            throw new ValidationException(violations);
        }

        student.setPassword(PasswordManager.hashPassword(student.getPassword()));

        studentDao.save(student);
    }

    public void updateStudent(Student student) {
    }


    private void validateStudent(Student student) {
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.stream().map(ConstraintViolation::getMessage).toList());
        }
    }

}
