package org.tutorBridge.dao;

import org.hibernate.Session;
import org.tutorBridge.entities.User;

import javax.persistence.TypedQuery;
import java.util.Optional;

public class UserDao extends GenericDao<User, Long> {
    public UserDao() {
        super(User.class);
    }

    public Optional<User> findByEmail(String email) {
        try (Session session = openSession()) {
            TypedQuery<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);
            return query.getResultList().stream().findFirst();
        }
    }
}
