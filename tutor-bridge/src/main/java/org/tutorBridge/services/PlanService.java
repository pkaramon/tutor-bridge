package org.tutorBridge.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dto.PlanEntryDTO;
import org.tutorBridge.dto.PlanResponseDTO;
import org.tutorBridge.dto.TimeFrameDTO;
import org.tutorBridge.entities.Reservation;
import org.tutorBridge.entities.Specialization;
import org.tutorBridge.entities.Student;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.repositories.ReservationRepo;
import org.tutorBridge.repositories.StudentRepo;
import org.tutorBridge.repositories.TutorRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {
    private final ReservationRepo reservationRepo;
    private final TutorRepo tutorRepo;
    private final StudentRepo studentRepo;

    public PlanService(ReservationRepo reservationRepo, TutorRepo tutorRepo, StudentRepo studentRepo) {
        this.reservationRepo = reservationRepo;
        this.tutorRepo = tutorRepo;
        this.studentRepo = studentRepo;
    }

    private static PlanResponseDTO fromReservationsToPlanResponseDTO(List<Reservation> reservations) {
        List<PlanEntryDTO> planEntries = reservations.stream().map(reservation -> {
            Student student = reservation.getStudent();
            Tutor tutor = reservation.getTutor();
            Specialization specialization = reservation.getSpecialization();
            return new PlanEntryDTO(
                    reservation.getReservationId(),
                    tutor.getUserId(),
                    reservation.getStartDateTime(),
                    reservation.getEndDateTime(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getPhone(),
                    student.getEmail(),
                    tutor.getFirstName(),
                    tutor.getLastName(),
                    tutor.getPhone(),
                    tutor.getEmail(),
                    specialization.getName(),
                    reservation.getStatus().toString()
            );
        }).collect(Collectors.toList());

        return new PlanResponseDTO(planEntries);
    }

    @Transactional(readOnly = true)
    public PlanResponseDTO getPlanForTutor(Tutor tutor, TimeFrameDTO timeframe) {
        timeframe = TimeFrameDTO.fillInEmptyFields(timeframe);
        List<Reservation> reservations = reservationRepo
                .findOverlapping(tutor, timeframe.getStart(), timeframe.getEnd());
        return fromReservationsToPlanResponseDTO(reservations);
    }

    @Transactional(readOnly = true)
    public PlanResponseDTO getPlanForStudent(Student student, TimeFrameDTO timeframe) {
        timeframe = TimeFrameDTO.fillInEmptyFields(timeframe);
        List<Reservation> reservations = reservationRepo
                .findOverlapping(student, timeframe.getStart(), timeframe.getEnd());
        return fromReservationsToPlanResponseDTO(reservations);
    }
}
