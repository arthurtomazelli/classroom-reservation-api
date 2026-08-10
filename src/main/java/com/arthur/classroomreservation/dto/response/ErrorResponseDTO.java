package com.arthur.classroomreservation.dto.response;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        String message
) {
}
