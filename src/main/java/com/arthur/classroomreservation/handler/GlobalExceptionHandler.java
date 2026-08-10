package com.arthur.classroomreservation.handler;

import com.arthur.classroomreservation.dto.response.ErrorResponseDTO;
import com.arthur.classroomreservation.exception.ClassroomAlreadyExistsException;
import com.arthur.classroomreservation.exception.ClassroomNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClassroomAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(ClassroomAlreadyExistsException exception) {
        return createEntity(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ClassroomNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(ClassroomNotFoundException exception) {
        return createEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return createEntity(message, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponseDTO> createEntity(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ErrorResponseDTO(
                        LocalDateTime.now(),
                        message
                ));
    }
}
