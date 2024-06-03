package org.tutorBridge;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.tutorBridge.entities.*;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Session session = getSessionFromCfg();
        Transaction transaction = session.beginTransaction();


//        addNewUser(session);
//        searchForStudents(session);
//        findAllTutors(session);
//        findAllSpecializations(session);
//        findAllAbsences(session);
//        findAllAvail(session);
        findAllReservations(session);

        transaction.commit();
        System.out.println("SUCCESS");
        session.close();
    }

    private static void searchForStudents(Session session) {
        TypedQuery<Student> query = session.createQuery("from Student s where s.firstName=:firstname", Student.class)
                .setParameter("firstname", "Adam");

        List<Student> students = query.getResultList();
        for (Student s : students)
            System.out.printf("This is %s %s%n", s.getFirstName(), s.getLastName());
    }

    private static void findAllTutors(Session session) {
        TypedQuery<Tutor> query = session.createQuery("from Tutor", Tutor.class);

        List<Tutor> tutors = query.getResultList();
        for (Tutor t : tutors) {
            System.out.println(
                    t.getFirstName() + " " + t.getLastName() + " " + t.getPhone() + " " + t.getEmail() + " " + t.getPassword() + " " + t.getBio() + " " + t.getBirthDate()
            );
            String specializations = t.getSpecializations().stream().map(Specialization::getName).collect(Collectors.joining());
            System.out.println(specializations);

            String absences = t.getAbsences().stream().map(a -> a.getStartDate().toString()).collect(Collectors.joining());
            System.out.println(absences);
        }

    }

    private static void findAllSpecializations(Session session) {
        TypedQuery<Specialization> query = session.createQuery("from Specialization", Specialization.class);
        List<Specialization> list = query.getResultList();
        for (Specialization o : list) {
            System.out.println(o.getName() + " " + o.getSpecializationId());
        }
    }

    private static void findAllAbsences(Session session) {
        TypedQuery<Absence> query = session.createQuery("from Absence", Absence.class);
        List<Absence> list = query.getResultList();
        for (Absence a : list) {
            System.out.println(a.getStartDate() + " " + a.getEndDate() + " " + a.getTutor().getFirstName());
        }
    }


    private static void findAllAvail(Session session) {
        TypedQuery<Availability> query = session.createQuery("from Availability", Availability.class);
        List<Availability> list = query.getResultList();
        for (Availability a : list) {
            System.out.println(a.getDate() + " " + a.getTutor().getFirstName() + " " + a.getEndMinute());
        }

    }

    private static void findAllReservations(Session session) {
        StringBuilder builder = new StringBuilder();
        TypedQuery<Reservation> query = session.createQuery("from Reservation ", Reservation.class);
        List<Reservation> list = query.getResultList();

        for (Reservation r : list) {
            builder.append(r.getStatus()).append(" ").append(r.getStudent().getFirstName()).append(" ").append(r.getTutor().getFirstName()).append(" ").append(r.getStartHour());
            builder.append("\n");
        }

        System.out.println(builder);


    }


    private static void addNewUser(Session session) {
        User u = new User(
                "John",
                "Doe",
                "123456789",
                "johndoe@gmail.com",
                "Password123",
                UserType.STUDENT,
                LocalDate.of(2000, 1, 4)
        );

        session.persist(u);
    }


    public static Session getSessionFromCfg() {
        Configuration config = new Configuration();
        config.configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        return sessionFactory.getCurrentSession();
    }
}
