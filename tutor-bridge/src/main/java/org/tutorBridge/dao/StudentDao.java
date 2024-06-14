package org.tutorBridge.dao;

import org.springframework.stereotype.Repository;
import org.tutorBridge.entities.Student;


@Repository
public class StudentDao extends GenericDao<Student, Long> {
    public StudentDao() {
        super(Student.class);
    }

}
