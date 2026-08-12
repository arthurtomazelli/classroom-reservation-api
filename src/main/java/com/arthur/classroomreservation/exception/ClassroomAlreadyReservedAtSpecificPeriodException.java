package com.arthur.classroomreservation.exception;

import java.time.LocalDateTime;
import java.util.UUID;

public final class ClassroomAlreadyReservedAtSpecificPeriodException extends RuntimeException implements DomainException {
    public ClassroomAlreadyReservedAtSpecificPeriodException(UUID classroomId, LocalDateTime startTime, LocalDateTime endTime) {
        super("Classroom already reserved at this time period. From: " + startTime + ", To: " + endTime + " - ID: " + classroomId);
    }
}
