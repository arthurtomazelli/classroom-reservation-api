package com.arthur.classroomreservation.controller;

import com.arthur.classroomreservation.dto.request.ClassroomRequestDTO;
import com.arthur.classroomreservation.dto.request.ClassroomUpdateRequestDTO;
import com.arthur.classroomreservation.dto.response.ClassroomResponseDTO;
import com.arthur.classroomreservation.service.ClassroomService;
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
@RequestMapping("/api/classrooms")
public class ClassroomController {
    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @PostMapping
    public ResponseEntity<ClassroomResponseDTO> create(
            @Valid @RequestBody ClassroomRequestDTO classroomRequestDTO
    ) {
        ClassroomResponseDTO response = classroomService.create(classroomRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> get(
            @PathVariable UUID id
    ) {
        ClassroomResponseDTO response = classroomService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ClassroomResponseDTO>> list(
            @RequestParam(required = false) Boolean available
    ) {
        List<ClassroomResponseDTO> response = classroomService.list(available);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ClassroomUpdateRequestDTO classroomUpdateRequestDTO
    ) {
        ClassroomResponseDTO response = classroomService.update(id, classroomUpdateRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ClassroomResponseDTO> activate(
            @PathVariable UUID id
    ) {
        ClassroomResponseDTO response = classroomService.activate(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ClassroomResponseDTO> deactivate(
            @PathVariable UUID id
    ) {
        ClassroomResponseDTO response = classroomService.deactivate(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/free")
    public ResponseEntity<List<ClassroomResponseDTO>> findFreeForReservation(
            @RequestParam() LocalDateTime startTime,
            @RequestParam() LocalDateTime endTime
    ) {
        List<ClassroomResponseDTO> response = classroomService.findFreeForReservation(startTime, endTime);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
