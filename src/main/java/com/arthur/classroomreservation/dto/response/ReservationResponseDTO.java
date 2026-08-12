package com.arthur.classroomreservation.dto.response;

import com.arthur.classroomreservation.entity.Classroom;
import com.arthur.classroomreservation.entity.Reservation;
import com.arthur.classroomreservation.entity.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponseDTO(
        UUID id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        ReservationStatus status,
        Classroom classroom
) {
    public static ReservationResponseDTO from(Reservation reservation) {
        return new ReservationResponseDTO(
                reservation.getId(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getStatus(),
                reservation.getClassroom()
        );
    }
}
