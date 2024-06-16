package org.tutorBridge.dao;

import org.springframework.stereotype.Repository;
import org.tutorBridge.entities.Reservation;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.entities.enums.ReservationStatus;

import java.time.LocalDateTime;

@Repository
public class ReservationDao extends GenericDao<Reservation, Long>{
    public ReservationDao() {
        super(Reservation.class);
    }

    public void cancelReservationsFor(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        em.createQuery("UPDATE Reservation r SET r.status = :status WHERE r.tutor = :tutor AND r.startDateTime >= :start AND r.endDateTime <= :end")
                .setParameter("status", ReservationStatus.CANCELLED)
                .setParameter("tutor", tutor)
                .setParameter("start", start)
                .setParameter("end", end)
                .executeUpdate();
    }
}
