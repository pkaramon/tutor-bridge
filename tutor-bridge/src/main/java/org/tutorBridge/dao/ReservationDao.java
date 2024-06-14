package org.tutorBridge.dao;

import org.springframework.stereotype.Repository;
import org.tutorBridge.entities.Reservation;

@Repository
public class ReservationDao extends GenericDao<Reservation, Long>{
    public ReservationDao() {
        super(Reservation.class);
    }

}
