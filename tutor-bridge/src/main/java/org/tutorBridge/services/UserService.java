package org.tutorBridge.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dao.UserDao;
import org.tutorBridge.entities.User;
import org.tutorBridge.security.PasswordManager;
import org.tutorBridge.validation.ValidationException;

import java.util.List;

@Service
public abstract class UserService<T extends User> extends AbstractService {
    private final UserDao userDao = new UserDao();

    public void registerUser(T user) {
        boolean userWithSameEmailExists = userDao.findByEmail(user.getEmail()).isPresent();
        if (userWithSameEmailExists) {
            throw new ValidationException(List.of("User with the same email already exists"));
        }
        user.setPassword(PasswordManager.hashPassword(user.getPassword()));
        saveUser(user);
    }


    protected abstract void saveUser(T user);
}

