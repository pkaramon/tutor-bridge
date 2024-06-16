package org.tutorBridge.repositories;

import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.tutorBridge.entities.Availability;
import org.tutorBridge.entities.Tutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AvailabilityRepo extends GenericRepo<Availability, Long> {
    public AvailabilityRepo() {
        super(Availability.class);
    }

    public Optional<Availability> findWithTutorAndSpecializations(Long availabilityId) {
        return em.createQuery("SELECT a FROM Availability a" +
                                " JOIN FETCH a.tutor t" +
                                " JOIN FETCH t.specializations" +
                                " WHERE a.availabilityId = :availabilityId",
                        Availability.class)
                .setParameter("availabilityId", availabilityId)
                .getResultStream()
                .findFirst();
    }

    public void deleteAvailabilitiesFor(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        em.createQuery("DELETE FROM Availability a WHERE a.tutor = :tutor AND a.startDateTime < :end AND a.endDateTime > :start")
                .setParameter("tutor", tutor)
                .setParameter("start", start)
                .setParameter("end", end)
                .executeUpdate();
    }


    public List<Availability> fetchAvailabilities(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        TypedQuery<Availability> query = em.createQuery(
                "FROM Availability a " +
                        "WHERE a.tutor = :tutor AND a.startDateTime < :end AND a.endDateTime > :start " +
                        "ORDER BY a.startDateTime ASC",
                Availability.class
        );
        query.setParameter("tutor", tutor);
        query.setParameter("start", start);
        query.setParameter("end", end);
        return query.getResultList();
    }


}
