package org.tutorBridge.dto;

import java.time.LocalDateTime;

public record PlanEntryDTO(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String studentFirstName,
        String studentLastName,
        String studentPhone,
        String studentEmail,
        String tutorFirstName,
        String tutorLastName,
        String tutorPhone,
        String tutorEmail,
        String specialization,
        String status
) {
}

