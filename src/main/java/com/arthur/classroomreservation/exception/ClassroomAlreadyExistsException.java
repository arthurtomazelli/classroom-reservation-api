package com.arthur.classroomreservation.exception;

import java.util.UUID;

public final class ClassroomAlreadyExistsException extends RuntimeException implements DomainException {
    public ClassroomAlreadyExistsException(String block, String number, UUID id) {
        super("Classroom already exists. Block: " + block + ", Number: " + number + " - ID: " + id);
    }
}
