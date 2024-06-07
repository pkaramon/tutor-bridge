package org.tutorBridge.service;

import org.tutorBridge.dao.StudentDao;
import org.tutorBridge.entities.Student;


public class StudentService extends UserService<Student> {
    private final StudentDao studentDao = new StudentDao();

    public void registerStudent(Student student) {
        registerUser(student);
    }

    @Override
    protected void saveUser(Student stuent) {
        studentDao.save(stuent);
    }
}
