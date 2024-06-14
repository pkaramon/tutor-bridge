package org.tutorBridge.dao;

import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.tutorBridge.entities.Absence;
import org.tutorBridge.entities.Availability;
import org.tutorBridge.entities.Reservation;
import org.tutorBridge.entities.Tutor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TutorDao extends GenericDao<Tutor, Long> {

    @PersistenceContext
    private EntityManager em;

    public TutorDao() {
        super(Tutor.class);
    }

    public List<Availability> fetchAvailabilities(Tutor tutor, LocalDate start, LocalDate end) {
        TypedQuery<Availability> query = em.createQuery(
                "FROM Availability a WHERE a.tutor = :tutor AND a.startDateTime < :end AND a.endDateTime > :start",
                Availability.class
        );
        query.setParameter("tutor", tutor);
        query.setParameter("start", start.atStartOfDay());
        query.setParameter("end", end.plusDays(1).atStartOfDay());
        return query.getResultList();
    }

    public boolean hasTutorConflictingAvailability(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        TypedQuery<Availability> query = em.createQuery(
                "FROM Availability a WHERE a.tutor = :tutor AND a.startDateTime < :end AND a.endDateTime > :start",
                Availability.class);
        query.setParameter("tutor", tutor);
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<Availability> results = query.getResultList();
        return !results.isEmpty();
    }

    public boolean isTutorAvailable(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        TypedQuery<Availability> query = em.createQuery(
                "FROM Availability a WHERE a.tutor = :tutor AND a.startDateTime <= :start AND a.endDateTime >= :end",
                Availability.class
        );
        query.setParameter("tutor", tutor);
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<Availability> results = query.getResultList();
        return !results.isEmpty();
    }

    public boolean hasAbsenceDuring(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        TypedQuery<Absence> query = em.createQuery(
                "FROM Absence a WHERE a.tutor = :tutor AND a.startDate <= :end AND a.endDate >= :start",
                Absence.class
        );
        query.setParameter("tutor", tutor);
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<Absence> results = query.getResultList();
        return !results.isEmpty();
    }

    public boolean hasConflictingReservation(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        TypedQuery<Reservation> query = em.createQuery(
                "FROM Reservation r WHERE r.tutor = :tutor AND r.startDateTime < :end AND r.endDateTime > :start",
                Reservation.class
        );
        query.setParameter("tutor", tutor);
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<Reservation> results = query.getResultList();
        return !results.isEmpty();
    }


    public boolean hasConflictingAbsence(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        TypedQuery<Absence> query = em.createQuery(
                "FROM Absence a WHERE a.tutor = :tutor AND a.startDate < :end AND a.endDate > :start",
                Absence.class
        );
        query.setParameter("tutor", tutor);
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<Absence> results = query.getResultList();
        return !results.isEmpty();
    }

    public List<Reservation> findReservationsAffectedByAbsence(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        TypedQuery<Reservation> query = em.createQuery(
                "FROM Reservation r WHERE r.tutor = :tutor AND r.startDateTime < :end AND r.endDateTime > :start",
                Reservation.class
        );
        query.setParameter("tutor", tutor);
        query.setParameter("start", start);
        query.setParameter("end", end);
        return query.getResultList();
    }

}
