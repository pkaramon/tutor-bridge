package org.tutorBridge.main;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.tutorBridge.entities.User;

public class Main {
    public static void main(String[] args) {
        // create entity manager factory
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        System.out.println(entityManager.find(User.class, 1L));

        entityManager.close();






//        StudentService studentService = new StudentService();
//        TutorService tutorService = new TutorService();
//        SpecializationRepo specializationDao = new SpecializationRepo();
//        TutorRepo tutorDao = new TutorRepo();
//        StudentRepo studentDao = new StudentRepo();
//        AbsenceService absenceService = new AbsenceService();


//        absenceService.addAbsence(
//                tutorDao.findById(3L).orElseThrow(),
//                LocalDateTime.of(2024, 7, 1, 9, 0, 0),
//                LocalDateTime.of(2024, 7, 31, 10, 0, 0)
//
//        );

//        DB.inTransaction(em -> { System.out.println(tutorDao.findById(3L, em));
//        });

//        try (var session = DB.getSessionFactory().openSession()) {
//            TypedQuery<Availability> query = session.createQuery(
//                    "FROM Availability a WHERE a.tutor = :tutor AND a.startDateTime <= :getStart AND a.endDateTime >= :getEnd",
//                    Availability.class
//            );
//
//            query.setParameter("tutor", tutorDao.findById(3L).orElseThrow());
//            query.setParameter("getStart", LocalDateTime.of(2024, 7, 5, 18, 0, 0));
//            query.setParameter("getEnd", LocalDateTime.of(2024, 7, 5, 20, 0, 0));
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
//        LocalDateTime getStart = LocalDateTime.of(2024, 7, 5, 18, 0, 0);
//        LocalDateTime getEnd = LocalDateTime.of(2024, 7, 5, 20, 0, 0);
//
//        try (var session = DB.getSessionFactory().openSession()) {
//            System.out.println(tutorDao.isTutorAvailable(tutor, getStart, getEnd, session));
//        }
//
//        Reservation r = new Reservation(student, tutor, specialization, getStart, getEnd);
//
//        studentService.makeReservations(r);


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

//        DB.close();
    }
}
