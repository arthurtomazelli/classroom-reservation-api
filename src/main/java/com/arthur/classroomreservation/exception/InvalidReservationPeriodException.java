package com.arthur.classroomreservation.exception;

import java.time.LocalDateTime;

public class InvalidReservationPeriodException extends RuntimeException {
    public InvalidReservationPeriodException(LocalDateTime startTime, LocalDateTime endTime) {
        super("Invalid reservation period. End time must be after start time. User input: From " + startTime + ", To " + endTime);
    }
}
