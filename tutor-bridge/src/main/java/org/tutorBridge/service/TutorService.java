package org.tutorBridge.service;

import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.entities.Tutor;

public class TutorService extends UserService<Tutor> {
    private final TutorDao tutorDao = new TutorDao();


    public void registerTutor(Tutor tutor) {
        registerUser(tutor);
    }

    @Override
    protected void saveUser(Tutor user) {
        tutorDao.save(user);
    }
}
