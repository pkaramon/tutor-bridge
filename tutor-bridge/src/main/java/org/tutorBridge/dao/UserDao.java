package org.tutorBridge.dao;

import org.tutorBridge.config.DB;
import org.tutorBridge.entities.User;

import jakarta.persistence.TypedQuery;
import java.util.Optional;

public class UserDao extends GenericDao<User, Long> {
    public UserDao() {
        super(User.class);
    }

    public Optional<User> findByEmail(String email) {

        return DB.withEntityManger(em -> {
            TypedQuery<User> query = em.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);
            return query.getResultList().stream().findFirst();
        });
    }
}
