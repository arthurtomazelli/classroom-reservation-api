package com.arthur.classroomreservation.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationRequestDTO(
        @Future @NotNull LocalDateTime startTime,
        @Future @NotNull LocalDateTime endTime,
        @NotNull UUID classroomId
) {
}
