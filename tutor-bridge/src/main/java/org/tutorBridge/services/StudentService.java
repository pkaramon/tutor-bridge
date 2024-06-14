package org.tutorBridge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tutorBridge.dao.ReservationDao;
import org.tutorBridge.dao.StudentDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.entities.Student;

import jakarta.persistence.EntityManager;


@Service
public class StudentService extends UserService<Student> {
    private final StudentDao studentDao ;

    public StudentService(StudentDao studentDao) {
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
