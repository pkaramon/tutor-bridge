package org.tutorBridge.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dao.StudentDao;
import org.tutorBridge.dao.UserDao;
import org.tutorBridge.entities.Student;


@Service
public class StudentService extends UserService<Student> {
    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao, UserDao userDao, PasswordEncoder passwordEncoder) {
        super(userDao, passwordEncoder);
        this.studentDao = studentDao;
    }


    @Transactional
    public void registerStudent(Student student) {
        registerUser(student);
    }

    @Transactional
    public void updateStudent(Student student) {
        studentDao.update(student);
    }


    @Override
    protected void saveUser(Student user) {
        studentDao.save(user);
    }
}
