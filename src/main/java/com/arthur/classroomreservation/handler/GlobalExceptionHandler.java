package com.arthur.classroomreservation.handler;

import com.arthur.classroomreservation.dto.response.ErrorResponseDTO;
import com.arthur.classroomreservation.exception.ClassroomAlreadyExistsException;
import com.arthur.classroomreservation.exception.ClassroomAlreadyReservedAtSpecificPeriodException;
import com.arthur.classroomreservation.exception.ClassroomNotFoundException;
import com.arthur.classroomreservation.exception.InvalidReservationPeriodException;
import com.arthur.classroomreservation.exception.ReservationNotFoundException;
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


    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(ReservationNotFoundException exception) {
        return createEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClassroomAlreadyReservedAtSpecificPeriodException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(ClassroomAlreadyReservedAtSpecificPeriodException exception) {
        return createEntity(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidReservationPeriodException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(InvalidReservationPeriodException exception) {
        return createEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
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
