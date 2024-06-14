package org.tutorBridge;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dao.SpecializationDao;
import org.tutorBridge.dao.UserDao;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.entities.User;

import java.time.LocalDate;
import java.util.List;


@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class}
)
@EntityScan(basePackages = "org.tutorBridge.entities")
@EnableJpaRepositories("org.tutorBridge.dao")
@EnableTransactionManagement
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PersistenceContext(type= PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Bean
    public CommandLineRunner demo(SpecializationDao specializationDao) {
        return (args) -> {


            var users = specializationDao.findAll();
            if (users.isEmpty()) {
                System.out.println("No users found.");
            } else {
                users.forEach(user -> System.out.println(user.getName()));
            }
        };
    }


}
