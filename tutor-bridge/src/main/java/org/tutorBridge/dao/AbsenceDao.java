package org.tutorBridge.dao;

import org.springframework.stereotype.Repository;
import org.tutorBridge.entities.Absence;

@Repository
public class AbsenceDao extends GenericDao<Absence, Long> {
    public AbsenceDao() {
        super(Absence.class);
    }
}
