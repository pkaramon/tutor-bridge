package org.tutorBridge.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dao.*;
import org.tutorBridge.dto.NewReservationDTO;
import org.tutorBridge.dto.StudentUpdateDTO;
import org.tutorBridge.entities.*;
import org.tutorBridge.validation.ValidationException;

import java.util.List;


@Service
public class StudentService extends UserService<Student> {
    private final StudentDao studentDao;
    private final AvailabilityDao availabilityDao;
    private final SpecializationDao specializationDao;
    private final TutorDao tutorDao;
    private final ReservationDao reservationDao;

    public StudentService(StudentDao studentDao, UserDao userDao, PasswordEncoder passwordEncoder, AvailabilityDao availabilityDao, SpecializationDao specializationDao, TutorDao tutorDao, ReservationDao reservationDao) {
        super(userDao, passwordEncoder);
        this.studentDao = studentDao;
        this.availabilityDao = availabilityDao;
        this.specializationDao = specializationDao;
        this.tutorDao = tutorDao;
        this.reservationDao = reservationDao;
    }


    @Transactional
    public void registerStudent(Student student) {
        registerUser(student);
    }

    @Transactional
    public void updateStudent(Student student) {
        studentDao.update(student);
    }


    @Override
    protected void saveUser(Student user) {
        studentDao.save(user);
    }

    @Transactional
    public void updateStudentInfo(String email, StudentUpdateDTO studentData) {
        Student student = getStudent(email);

        if (studentData.getFirstName() != null) student.setFirstName(studentData.getFirstName());
        if (studentData.getLastName() != null) student.setLastName(studentData.getLastName());
        if (studentData.getPhone() != null) student.setPhone(studentData.getPhone());
        if (studentData.getEmail() != null) student.setEmail(studentData.getEmail());
        if (studentData.getBirthDate() != null) student.setBirthDate(studentData.getBirthDate());
        if (studentData.getLevel() != null) student.setLevel(studentData.getLevel());

        studentDao.update(student);
    }


    @Transactional
    public void makeReservations(String email, List<NewReservationDTO> reservationsData) {
        Student student = getStudent(email);
        for (NewReservationDTO reservationData : reservationsData) {
            makeReservation(student, reservationData);
        }
        studentDao.update(student);
    }

    private void makeReservation(Student student, NewReservationDTO data) {
        Availability slot = availabilityDao.findWithTutorAndSpecializations(data.getAvailabilityId())
                .orElseThrow(() -> new ValidationException("Availability not found"));
        Tutor tutor = slot.getTutor();
        Specialization specialization = tutor.getSpecializations().stream()
                .filter(s -> s.getSpecializationId().equals(data.getSpecializationId()))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Specialization not found"));

        Reservation reservation = new Reservation(
                student,
                tutor,
                specialization,
                slot.getStartDateTime(),
                slot.getEndDateTime()
        );
        student.addReservation(reservation);
        tutor.addReservation(reservation);

        studentDao.save(student);
        tutorDao.update(tutor);
        reservationDao.save(reservation);

        availabilityDao.deleteAvailabilitiesFor(tutor, slot.getStartDateTime(), slot.getEndDateTime());
    }

    private Student getStudent(String email) {
        Student student = studentDao.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Student not found"));
        return student;
    }
}
