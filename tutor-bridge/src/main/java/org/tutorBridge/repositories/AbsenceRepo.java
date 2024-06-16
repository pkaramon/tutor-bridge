package org.tutorBridge.repositories;

import org.springframework.stereotype.Repository;
import org.tutorBridge.entities.Absence;
import org.tutorBridge.entities.Tutor;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AbsenceRepo extends GenericRepo<Absence, Long> {
    public AbsenceRepo() {
        super(Absence.class);
    }


    public List<Absence> fetchAbsences(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        return em.createQuery("FROM Absence a WHERE a.tutor = :tutor AND a.startDate >= :start AND a.endDate <= :end", Absence.class)
                .setParameter("tutor", tutor)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    public List<Absence> fetchAbsences(Tutor tutor) {
        return em.createQuery("FROM Absence a WHERE a.tutor = :tutor", Absence.class)
                .setParameter("tutor", tutor)
                .getResultList();
    }
}