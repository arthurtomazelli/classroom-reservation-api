package com.arthur.classroomreservation.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationUpdateRequestDTO(
        LocalDateTime startTime,
        LocalDateTime endTime,
        UUID classroomId
) {
}
