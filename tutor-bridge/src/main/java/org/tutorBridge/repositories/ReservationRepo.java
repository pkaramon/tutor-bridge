package org.tutorBridge.repositories;

import org.springframework.stereotype.Repository;
import org.tutorBridge.entities.Reservation;
import org.tutorBridge.entities.Student;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.entities.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationRepo extends GenericRepo<Reservation, Long> {
    public ReservationRepo() {
        super(Reservation.class);
    }

    public void cancelReservationsFor(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        em.createQuery("UPDATE Reservation r SET r.status = :status" +
                        " WHERE r.tutor = :tutor AND r.startDateTime < :end AND r.endDateTime > :start")
                .setParameter("status", ReservationStatus.CANCELLED)
                .setParameter("tutor", tutor)
                .setParameter("start", start)
                .setParameter("end", end)
                .executeUpdate();
    }

    public void updateReservationStatus(Long reservationId, ReservationStatus status) {
        em.createQuery("UPDATE Reservation r SET r.status = :status WHERE r.reservationId = :reservationId")
                .setParameter("status", status)
                .setParameter("reservationId", reservationId)
                .executeUpdate();
    }

    public List<Reservation> findReservationsByTutorAndIds(Tutor tutor, List<Long> reservationIds) {
        return em.createQuery("FROM Reservation r WHERE r.tutor = :tutor AND r.reservationId IN :reservationIds", Reservation.class)
                .setParameter("tutor", tutor)
                .setParameter("reservationIds", reservationIds)
                .getResultList();
    }

    public void changeStatus(Long reservationId, ReservationStatus status) {
        em.createQuery("UPDATE Reservation r SET r.status = :status WHERE r.reservationId = :reservationId")
                .setParameter("status", status)
                .setParameter("reservationId", reservationId)
                .executeUpdate();
    }

    public List<Reservation> findValidReservationsFor(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        return em.createQuery("FROM Reservation r " +
                                "WHERE r.tutor = :tutor AND r.startDateTime < :end AND r.endDateTime > :start " +
                                "AND r.status != :status",
                        Reservation.class)
                .setParameter("tutor", tutor)
                .setParameter("start", start)
                .setParameter("end", end)
                .setParameter("status", ReservationStatus.CANCELLED)
                .getResultList();
    }

    public List<Reservation> findReservationsForStudent(Student student, LocalDateTime start, LocalDateTime end) {
        return em.createQuery("FROM Reservation r " +
                                "WHERE r.student = :student AND r.startDateTime < :end AND r.endDateTime > :start ",
                        Reservation.class)
                .setParameter("student", student)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}
