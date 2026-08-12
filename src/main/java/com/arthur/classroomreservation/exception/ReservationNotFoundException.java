package com.arthur.classroomreservation.exception;

import java.util.UUID;

public final class ReservationNotFoundException extends RuntimeException implements DomainException {
    public ReservationNotFoundException(UUID id) {
        super("Reservation not found. ID: " + id);
    }
}
