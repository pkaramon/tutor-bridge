package org.tutorBridge.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tutorBridge.dao.UserDao;
import org.tutorBridge.entities.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userDao.findById(Long.parseLong(userId)).orElseThrow(() ->
                new UsernameNotFoundException("User not found with id: " + userId));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserId().toString())
                .password(user.getPassword())
                .authorities(user.getType().toString())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
