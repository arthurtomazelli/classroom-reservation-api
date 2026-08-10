package com.arthur.classroomreservation.exception;

import java.util.UUID;

public class ClassroomAlreadyExistsException extends RuntimeException {
    public ClassroomAlreadyExistsException(String block, String number, UUID id) {
        super("Classroom already exists. Block: " + block + ", Number: " + number + " - ID: " + id);
    }
}
