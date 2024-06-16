package org.tutorBridge.repositories;

import org.springframework.stereotype.Repository;
import org.tutorBridge.entities.Absence;

@Repository
public class AbsenceRepo extends GenericRepo<Absence, Long> {
    public AbsenceRepo() {
        super(Absence.class);
    }
}
