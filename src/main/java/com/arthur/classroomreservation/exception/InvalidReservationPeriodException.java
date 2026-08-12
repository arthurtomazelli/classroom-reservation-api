package com.arthur.classroomreservation.exception;

import java.time.LocalDateTime;

public final class InvalidReservationPeriodException extends RuntimeException implements DomainException {
    public InvalidReservationPeriodException(LocalDateTime startTime, LocalDateTime endTime) {
        super("Invalid reservation period. End time must be after start time. User input: From " + startTime + ", To " + endTime);
    }
}
