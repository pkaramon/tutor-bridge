package org.tutorBridge.dao;

import org.tutorBridge.entities.Specialization;

public class SpecializationDao  extends GenericDao<Specialization, Long>{
    public SpecializationDao() {
        super(Specialization.class);
    }
}
