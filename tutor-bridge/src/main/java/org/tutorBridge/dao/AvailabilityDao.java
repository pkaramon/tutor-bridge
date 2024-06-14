package org.tutorBridge.dao;

import org.springframework.stereotype.Repository;
import org.tutorBridge.entities.Availability;

@Repository
public class AvailabilityDao extends GenericDao<Availability, Long> {
    public AvailabilityDao() {
        super(Availability.class);
    }
}
