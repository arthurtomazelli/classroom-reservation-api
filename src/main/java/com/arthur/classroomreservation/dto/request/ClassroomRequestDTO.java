package com.arthur.classroomreservation.dto.request;

import com.arthur.classroomreservation.entity.enums.ClassroomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClassroomRequestDTO(
        @NotBlank String block,
        @NotBlank String number,
        @NotNull @Min(1) Integer capacity,
        @NotNull ClassroomType type) {
}
