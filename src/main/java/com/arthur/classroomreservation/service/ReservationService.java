package com.arthur.classroomreservation.service;

import com.arthur.classroomreservation.dto.request.ReservationRequestDTO;
import com.arthur.classroomreservation.dto.response.ReservationResponseDTO;
import com.arthur.classroomreservation.entity.Classroom;
import com.arthur.classroomreservation.entity.Reservation;
import com.arthur.classroomreservation.entity.enums.ReservationStatus;
import com.arthur.classroomreservation.exception.ClassroomAlreadyReservedAtSpecificPeriodException;
import com.arthur.classroomreservation.exception.InvalidReservationPeriodException;
import com.arthur.classroomreservation.repository.ReservationRepository;
import com.arthur.classroomreservation.util.DateTimeUtils;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ClassroomService classroomService;

    private static final ReservationStatus SET_STATUS_CONFIRMED = ReservationStatus.CONFIRMED;
    private static final ReservationStatus SET_STATUS_CANCELLED = ReservationStatus.CANCELLED;

    public ReservationService(ReservationRepository reservationRepository, ClassroomService classroomService) {
        this.reservationRepository = reservationRepository;
        this.classroomService = classroomService;
    }

    public ReservationResponseDTO create(ReservationRequestDTO request) {
        Classroom classroom = classroomService.getEntityById(request.classroomId());

        if(!DateTimeUtils.isEndAfterStart(request.startTime(), request.endTime())){
            throw new InvalidReservationPeriodException(request.startTime(), request.endTime());
        }

        if(!reservationRepository.findConflictingReservations(request.classroomId(), request.startTime(), request.endTime()).isEmpty()){
            throw new ClassroomAlreadyReservedAtSpecificPeriodException(
                    request.classroomId(),
                    request.startTime(),
                    request.endTime());
        }

        return ReservationResponseDTO.from(reservationRepository.save(
                Reservation.builder()
                        .startTime(request.startTime())
                        .endTime(request.endTime())
                        .status(SET_STATUS_CONFIRMED)
                        .classroom(classroom)
                        .build()
                )
        );
    }
}
