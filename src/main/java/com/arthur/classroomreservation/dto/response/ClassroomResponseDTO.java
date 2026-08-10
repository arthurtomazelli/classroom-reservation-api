package com.arthur.classroomreservation.dto.response;

import com.arthur.classroomreservation.entity.Classroom;
import com.arthur.classroomreservation.entity.enums.ClassroomType;

import java.util.UUID;

public record ClassroomResponseDTO(
        UUID id,
        String block,
        String number,
        Integer capacity,
        ClassroomType type,
        Boolean available
) {
    public static ClassroomResponseDTO from(Classroom classroom) {
        return new ClassroomResponseDTO(
                classroom.getId(),
                classroom.getBlock(),
                classroom.getNumber(),
                classroom.getCapacity(),
                classroom.getType(),
                classroom.getAvailable()
        );
    }
}
