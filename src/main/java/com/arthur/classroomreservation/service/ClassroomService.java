package com.arthur.classroomreservation.service;

import com.arthur.classroomreservation.dto.request.ClassroomRequestDTO;
import com.arthur.classroomreservation.dto.request.ClassroomUpdateRequestDTO;
import com.arthur.classroomreservation.dto.response.ClassroomResponseDTO;
import com.arthur.classroomreservation.entity.Classroom;
import com.arthur.classroomreservation.exception.ClassroomAlreadyExistsException;
import com.arthur.classroomreservation.exception.ClassroomNotFoundException;
import com.arthur.classroomreservation.repository.ClassroomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public ClassroomResponseDTO create(ClassroomRequestDTO request) {
        classroomRepository.findByBlockAndNumber(request.block(), request.number())
                .ifPresent(existing -> {
                    throw new ClassroomAlreadyExistsException(
                            existing.getBlock(),
                            existing.getNumber(),
                            existing.getId()
                    );
                });

        return ClassroomResponseDTO.from(classroomRepository.save(
                Classroom.builder()
                        .block(request.block())
                        .number(request.number())
                        .capacity(request.capacity())
                        .type(request.type())
                        .available(true)
                        .build())
        );
    }

    public ClassroomResponseDTO update(UUID id, ClassroomUpdateRequestDTO request) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ClassroomNotFoundException(id));

        applyUpdates(classroom, request);

        return ClassroomResponseDTO.from(classroomRepository.save(classroom));
    }

    private void applyUpdates(Classroom classroom, ClassroomUpdateRequestDTO request) {
        if(request.block() != null) classroom.setBlock(request.block());
        if(request.number() != null) classroom.setNumber(request.number());
        if(request.capacity() != null) classroom.setCapacity(request.capacity());
        if(request.type() != null) classroom.setType(request.type());
    }

    public ClassroomResponseDTO findById(UUID id) {
        return ClassroomResponseDTO.from(
                classroomRepository.findById(id)
                        .orElseThrow(() -> new ClassroomNotFoundException(id))) ;
    }

    public ClassroomResponseDTO deactivate(UUID id) {
        return changeAvailability(id, false);
    }

    public ClassroomResponseDTO activate(UUID id) {
        return changeAvailability(id, true);
    }

    private ClassroomResponseDTO changeAvailability(UUID id, boolean available) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ClassroomNotFoundException(id));

        classroom.setAvailable(available);

        return ClassroomResponseDTO.from(classroomRepository.save(classroom));
    }

    public List<ClassroomResponseDTO> list(Boolean available) {
        return classroomRepository.findByAvailableOptional(available)
                .stream()
                .map(ClassroomResponseDTO::from)
                .toList();
    }

    public List<ClassroomResponseDTO> findAvailableForReservation(LocalDateTime startTime, LocalDateTime endTime) {
        return classroomRepository.findAvailableForReservation(startTime, endTime)
                .stream()
                .map(ClassroomResponseDTO::from)
                .toList();
    }

    public Classroom getEntityById(UUID id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new ClassroomNotFoundException(id));
    }
}
