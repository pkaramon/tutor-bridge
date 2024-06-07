package org.tutorBridge.dao;

import org.tutorBridge.entities.Absence;

public class AbsenceDao extends GenericDao<Absence, Long> {
    public AbsenceDao() {
        super(Absence.class);
    }
}
