package com.arthur.classroomreservation.controller;

import com.arthur.classroomreservation.dto.request.ReservationRequestDTO;
import com.arthur.classroomreservation.dto.request.ReservationUpdateRequestDTO;
import com.arthur.classroomreservation.dto.response.ReservationResponseDTO;
import com.arthur.classroomreservation.entity.enums.ReservationStatus;
import com.arthur.classroomreservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> create(
            @Valid @RequestBody ReservationRequestDTO request
    ) {
        ReservationResponseDTO response = reservationService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> get(
            @PathVariable UUID id
    ) {
        ReservationResponseDTO response = reservationService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> list(
            @RequestParam(required = false) UUID classroomId,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime,
            @RequestParam(required = false) ReservationStatus status
    ) {
        List<ReservationResponseDTO> response = reservationService.list(classroomId, startTime, endTime, status);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ReservationUpdateRequestDTO request
    ) {
        ReservationResponseDTO response = reservationService.update(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponseDTO> cancel(
            @PathVariable UUID id
    ) {
        ReservationResponseDTO response = reservationService.cancel(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
