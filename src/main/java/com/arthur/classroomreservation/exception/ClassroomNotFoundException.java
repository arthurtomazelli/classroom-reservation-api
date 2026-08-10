package com.arthur.classroomreservation.exception;

import java.util.UUID;

public class ClassroomNotFoundException extends RuntimeException {
    public ClassroomNotFoundException(UUID id) {
        super("Classroom not found. ID: " + id);
    }
}
