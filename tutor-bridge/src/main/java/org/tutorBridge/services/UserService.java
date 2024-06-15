package org.tutorBridge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tutorBridge.dao.UserDao;
import org.tutorBridge.entities.User;
import org.tutorBridge.validation.ValidationException;

import java.util.List;


@Service
public abstract class UserService<T extends User> extends AbstractService {
    protected final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    protected UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    protected void registerUser(T user) {
        boolean userWithSameEmailExists = userDao.findByEmail(user.getEmail()).isPresent();
        if (userWithSameEmailExists) {
            throw new ValidationException(List.of("User with the same email already exists"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
        saveUser(user);
    }

    protected abstract void saveUser(T user);
}
