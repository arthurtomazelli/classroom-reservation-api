package com.arthur.classroomreservation.service;

import com.arthur.classroomreservation.dto.request.ClassroomRequestDTO;
import com.arthur.classroomreservation.dto.response.ClassroomResponseDTO;
import com.arthur.classroomreservation.entity.Classroom;
import com.arthur.classroomreservation.exception.ClassroomAlreadyExistsException;
import com.arthur.classroomreservation.exception.ClassroomNotFoundException;
import com.arthur.classroomreservation.repository.ClassroomRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public ClassroomResponseDTO create(ClassroomRequestDTO request) {
        checkDuplicate(request.block(), request.number());

        Classroom classroom = classroomRepository.save(Classroom.builder()
                .block(request.block())
                .number(request.number())
                .capacity(request.capacity())
                .type(request.type())
                .available(true)
                .build());

        return ClassroomResponseDTO.from(classroom);
    }

    private void checkDuplicate(String block, String number){
        classroomRepository.findByBlockAndNumber(block, number)
                .ifPresent(existing -> {
                    throw new ClassroomAlreadyExistsException(
                            existing.getBlock(),
                            existing.getNumber(),
                            existing.getId()
                    );
                });
    }

    public ClassroomResponseDTO findById(UUID id) {
        return ClassroomResponseDTO.from(
                classroomRepository.findById(id)
                .orElseThrow(() -> new ClassroomNotFoundException(id))) ;
    }
}
