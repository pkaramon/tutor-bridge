package org.tutorBridge.dao;

import org.tutorBridge.entities.Reservation;

public class ReservationDao extends GenericDao<Reservation, Long>{
    public ReservationDao() {
        super(Reservation.class);
    }

}
