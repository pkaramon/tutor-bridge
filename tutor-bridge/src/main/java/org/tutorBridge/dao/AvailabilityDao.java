package org.tutorBridge.dao;

import org.tutorBridge.entities.Availability;

public class AvailabilityDao extends GenericDao<Availability, Long>{
    public AvailabilityDao() {
        super(Availability.class);
    }
}
