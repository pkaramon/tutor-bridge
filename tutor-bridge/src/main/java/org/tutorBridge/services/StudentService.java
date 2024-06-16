package org.tutorBridge.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dto.NewReservationDTO;
import org.tutorBridge.dto.StudentUpdateDTO;
import org.tutorBridge.entities.*;
import org.tutorBridge.repositories.*;
import org.tutorBridge.validation.ValidationException;

import java.util.List;


@Service
public class StudentService extends UserService<Student> {
    private final StudentRepo studentRepo;
    private final AvailabilityRepo availabilityRepo;
    private final TutorRepo tutorRepo;
    private final ReservationRepo reservationRepo;

    public StudentService(StudentRepo studentRepo, UserRepo userDao, PasswordEncoder passwordEncoder, AvailabilityRepo availabilityRepo, SpecializationRepo specializationDao, TutorRepo tutorRepo, ReservationRepo reservationRepo) {
        super(userDao, passwordEncoder);
        this.studentRepo = studentRepo;
        this.availabilityRepo = availabilityRepo;
        this.tutorRepo = tutorRepo;
        this.reservationRepo = reservationRepo;
    }

    private static StudentUpdateDTO fromStudentToDTO(Student student) {
        return new StudentUpdateDTO(
                student.getFirstName(),
                student.getLastName(),
                student.getPhone(),
                student.getBirthDate(),
                student.getLevel()
        );
    }

    @Transactional
    public void registerStudent(Student student) {
        registerUser(student);
    }

    @Override
    protected void saveUser(Student user) {
        studentRepo.save(user);
    }

    @Transactional
    public StudentUpdateDTO updateStudentInfo(String email, StudentUpdateDTO studentData) {
        Student student = getStudent(email);

        if (studentData.getFirstName() != null) student.setFirstName(studentData.getFirstName());
        if (studentData.getLastName() != null) student.setLastName(studentData.getLastName());
        if (studentData.getPhone() != null) student.setPhone(studentData.getPhone());
        if (studentData.getBirthDate() != null) student.setBirthDate(studentData.getBirthDate());
        if (studentData.getLevel() != null) student.setLevel(studentData.getLevel());

        studentRepo.update(student);

        return fromStudentToDTO(student);
    }

    @Transactional
    public void makeReservations(String email, List<NewReservationDTO> reservationsData) {
        Student student = getStudent(email);
        for (NewReservationDTO reservationData : reservationsData) {
            makeReservation(student, reservationData);
        }
        studentRepo.update(student);
    }

    private void makeReservation(Student student, NewReservationDTO data) {
        Availability slot = availabilityRepo.findWithTutorAndSpecializations(data.getAvailabilityId())
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

        studentRepo.save(student);
        tutorRepo.update(tutor);
        reservationRepo.save(reservation);

        availabilityRepo.deleteAvailabilitiesFor(tutor, slot.getStartDateTime(), slot.getEndDateTime());
    }

    private Student getStudent(String email) {
        Student student = studentRepo.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Student not found"));
        return student;
    }

    public StudentUpdateDTO getStudentInfo(String email) {
        Student student = getStudent(email);
        return fromStudentToDTO(student);
    }
}
