package org.tutorBridge.dao;

import org.tutorBridge.entities.Student;


public class StudentDao extends GenericDao<Student, Long> {
    public StudentDao() {
        super(Student.class);
    }
}
