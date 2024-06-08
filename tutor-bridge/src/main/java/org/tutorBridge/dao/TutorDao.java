package org.tutorBridge.dao;

import org.tutorBridge.entities.Availability;
import org.tutorBridge.entities.Tutor;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class TutorDao extends GenericDao<Tutor, Long> {
    public TutorDao() {
        super(Tutor.class);
    }

    public List<Availability> fetchAvailabilities(Tutor tutor, LocalDate start, LocalDate end) {
        try (var session = openSession()) {
            TypedQuery<Availability> query = session.createQuery(
                    "FROM Availability a WHERE a.tutor = :tutor AND a.startDateTime < :end AND a.endDateTime > :start",
                    Availability.class
            );
            query.setParameter("tutor", tutor);
            query.setParameter("start", start.atStartOfDay());
            query.setParameter("end", end.plusDays(1).atStartOfDay());
            return query.getResultList();
        }


    }
}
