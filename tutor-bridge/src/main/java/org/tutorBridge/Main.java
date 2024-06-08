package org.tutorBridge;

import org.tutorBridge.config.HibernateUtil;
import org.tutorBridge.dao.SpecializationDao;
import org.tutorBridge.dao.StudentDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.entities.Reservation;
import org.tutorBridge.entities.Specialization;
import org.tutorBridge.entities.Student;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.service.AbsenceService;
import org.tutorBridge.service.StudentService;
import org.tutorBridge.service.TutorService;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        StudentService studentService = new StudentService();
        TutorService tutorService = new TutorService();
        SpecializationDao specializationDao = new SpecializationDao();
        TutorDao tutorDao = new TutorDao();
        StudentDao studentDao = new StudentDao();
        AbsenceService absenceService = new AbsenceService();

//        absenceService.addAbsence(
//                tutorDao.findById(3L).orElseThrow(),
//                LocalDateTime.of(2024, 7, 1, 9, 0, 0),
//                LocalDateTime.of(2024, 7, 31, 10, 0, 0)
//
//        );

//        try (var session = HibernateUtil.getSessionFactory().openSession()) {
//            TypedQuery<Availability> query = session.createQuery(
//                    "FROM Availability a WHERE a.tutor = :tutor AND a.startDateTime <= :start AND a.endDateTime >= :end",
//                    Availability.class
//            );
//
//            query.setParameter("tutor", tutorDao.findById(3L).orElseThrow());
//            query.setParameter("start", LocalDateTime.of(2024, 7, 5, 18, 0, 0));
//            query.setParameter("end", LocalDateTime.of(2024, 7, 5, 20, 0, 0));
//
//            List<Availability> results = query.getResultList();
//            System.out.println(results.size());
//            for (Availability a : results) {
//                System.out.println(a.getStartDateTime() + " " + a.getEndDateTime());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        Student student = studentDao.findById(1L).orElseThrow();
//        Tutor tutor = tutorDao.findById(3L).orElseThrow();
//        Specialization specialization = specializationDao.findById(1L).orElseThrow();
//        LocalDateTime start = LocalDateTime.of(2024, 7, 5, 18, 0, 0);
//        LocalDateTime end = LocalDateTime.of(2024, 7, 5, 20, 0, 0);
//
//        try (var session = HibernateUtil.getSessionFactory().openSession()) {
//            System.out.println(tutorDao.isTutorAvailable(tutor, start, end, session));
//        }
//
//        Reservation r = new Reservation(student, tutor, specialization, start, end);
//
//        studentService.makeReservation(r);


//        Tutor tutor = tutorDao.findById(3L).orElseThrow();
//        Map<DayOfWeek, List<TimeRange>> weekelyTimeRanges = new HashMap<>();
//
//        weekelyTimeRanges.put(DayOfWeek.MONDAY, List.of(
//                new TimeRange(LocalTime.of(9, 15), LocalTime.of(10, 15))
//        ));
//
//
//        tutorService.addWeeklyAvailability(tutor, weekelyTimeRanges, 4);
        System.out.println("OK");
    }
}
