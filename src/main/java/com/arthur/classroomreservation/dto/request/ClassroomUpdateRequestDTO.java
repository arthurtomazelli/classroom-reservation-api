package com.arthur.classroomreservation.dto.request;

import com.arthur.classroomreservation.entity.enums.ClassroomType;
import jakarta.validation.constraints.Min;

public record ClassroomUpdateRequestDTO (
        String block,
        String number,
        @Min(1) Integer capacity,
        ClassroomType type){
}
