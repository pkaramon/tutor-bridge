package org.tutorBridge.service;

import org.tutorBridge.dao.ReservationDao;
import org.tutorBridge.dao.StudentDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.entities.Student;

import jakarta.persistence.EntityManager;


public class StudentService extends UserService<Student> {
    private final StudentDao studentDao = new StudentDao();
    private final TutorDao tutorDao = new TutorDao();
    private final ReservationDao reservationDao = new ReservationDao();


    public void registerStudent(Student student) {
        registerUser(student);
    }

    public void updateStudent(Student student) {
        studentDao.update(student);
    }

    @Override
    protected void saveUser(Student student, EntityManager em) {
        studentDao.save(student, em);
    }

}
