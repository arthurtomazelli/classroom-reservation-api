package com.arthur.classroomreservation.exception;

import java.util.UUID;

public final class ClassroomNotFoundException extends RuntimeException implements DomainException {
    public ClassroomNotFoundException(UUID id) {
        super("Classroom not found. ID: " + id);
    }
}
