package org.tutorBridge.dao;

import org.hibernate.procedure.ProcedureCall;
import org.tutorBridge.entities.Reservation;

import javax.persistence.ParameterMode;

public class ReservationDao extends GenericDao<Reservation, Long>{
    public ReservationDao() {
        super(Reservation.class);
    }

    public void addReservation(Reservation reservation) {
        try(var session = openSession()) {


//            ProcedureCall procedureCall = session.createStoredProcedureCall("p_add_reservation");
//            procedureCall.registerParameter("vstudentid", Long.class, ParameterMode.IN).bindValue(reservation.getStudent().getUserId());
//            procedureCall.registerParameter("vtutorid", Long.class, ParameterMode.IN).bindValue(reservation.getTutor().getUserId());
//            procedureCall.registerParameter("vspecializationid", Long.class, ParameterMode.IN).bindValue(reservation.getSpecialization().getSpecializationId());
//            procedureCall.registerParameter("vdate", java.sql.Date.class, ParameterMode.IN).bindValue();
//            procedureCall.registerParameter("vstarthour", Integer.class, ParameterMode.IN).bindValue(startHour);
//            procedureCall.registerParameter("vstartminute", Integer.class, ParameterMode.IN).bindValue(startMinute);
//            procedureCall.registerParameter("vendhour", Integer.class, ParameterMode.IN).bindValue(endHour);
//            procedureCall.registerParameter("vendminute", Integer.class, ParameterMode.IN).bindValue(endMinute);
//

            session.beginTransaction();


            ProcedureCall call = session.createStoredProcedureCall("P_ADD_RESERVATION");
            session.getTransaction().commit();
        }

    }
}
