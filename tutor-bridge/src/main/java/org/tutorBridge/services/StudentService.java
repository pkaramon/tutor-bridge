package org.tutorBridge.services;

import org.springframework.stereotype.Service;
import org.tutorBridge.dao.StudentDao;
import org.tutorBridge.dao.UserDao;
import org.tutorBridge.entities.Student;


@Service
public class StudentService extends UserService<Student> {
    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao, UserDao userDao) {
        super(userDao);
        this.studentDao = studentDao;
    }


    public void registerStudent(Student student) {
        registerUser(student);
    }

    public void updateStudent(Student student) {
        studentDao.update(student);
    }


    @Override
    protected void saveUser(Student user) {
        studentDao.save(user);
    }
}
