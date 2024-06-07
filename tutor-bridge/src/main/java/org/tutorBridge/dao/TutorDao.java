package org.tutorBridge.dao;

import org.tutorBridge.entities.Tutor;

public class TutorDao extends GenericDao<Tutor, Long> {
    public TutorDao() {
        super(Tutor.class);
    }
}
