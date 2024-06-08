package org.tutorBridge.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.tutorBridge.dao.ReservationDao;
import org.tutorBridge.dao.StudentDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.entities.Reservation;
import org.tutorBridge.entities.Student;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.validation.ValidationException;

import java.time.LocalDateTime;


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


    public void makeReservation(Reservation reservation) {
        Tutor tutor = reservation.getTutor();
        LocalDateTime start = reservation.getStartDateTime();
        LocalDateTime end = reservation.getEndDateTime();

        try(Session session = openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                if (!tutorDao.isTutorAvailable(tutor, start, end, session)) {
                    throw new ValidationException("Tutor is not available at the requested time.");
                }
                if (tutorDao.hasAbsenceDuring(tutor, start, end, session)) {
                    throw new ValidationException("Tutor has an absence record for the requested time.");
                }
                if (tutorDao.hasConflictingReservation(tutor, start, end, session)) {
                    throw new ValidationException("Tutor has another reservation at the requested time.");
                }
                reservationDao.save(reservation, session);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }


    @Override
    protected void saveUser(Student student) {
        studentDao.save(student);
    }

}
