package com.arthur.classroomreservation.service;

import com.arthur.classroomreservation.dto.request.ReservationRequestDTO;
import com.arthur.classroomreservation.dto.request.ReservationUpdateRequestDTO;
import com.arthur.classroomreservation.dto.response.ReservationResponseDTO;
import com.arthur.classroomreservation.entity.Classroom;
import com.arthur.classroomreservation.entity.Reservation;
import com.arthur.classroomreservation.entity.enums.ReservationStatus;
import com.arthur.classroomreservation.exception.ClassroomAlreadyReservedAtSpecificPeriodException;
import com.arthur.classroomreservation.exception.InvalidReservationPeriodException;
import com.arthur.classroomreservation.exception.ReservationNotFoundException;
import com.arthur.classroomreservation.repository.ReservationRepository;
import com.arthur.classroomreservation.util.DateTimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    @Transactional
    public ReservationResponseDTO create(ReservationRequestDTO request) {
        Classroom classroom = classroomService.getEntityForUpdate(request.classroomId());

        checkReservationPossible(request.startTime(), request.endTime(), request.classroomId());

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

    public ReservationResponseDTO update(UUID id, ReservationUpdateRequestDTO request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        applyUpdates(reservation, request);
        checkReservationPossible(reservation.getId(), reservation.getStartTime(), reservation.getEndTime(), reservation.getClassroom().getId());

        return ReservationResponseDTO.from(reservationRepository.save(reservation));
    }

    private void applyUpdates(Reservation reservation, ReservationUpdateRequestDTO request) {
        if(request.startTime() != null) reservation.setStartTime(request.startTime());
        if(request.endTime() != null) reservation.setEndTime(request.endTime());
        if(request.classroomId() != null) reservation.setClassroom(
                classroomService.getEntityById(request.classroomId())
        );
    }

    private void checkReservationPossible(LocalDateTime startTime, LocalDateTime endTime, UUID classroomId) {
        checkReservationPossible(null, startTime, endTime, classroomId);
    }

    private void checkReservationPossible(UUID id, LocalDateTime startTime, LocalDateTime endTime, UUID classroomId) {
        if(!DateTimeUtils.isEndAfterStart(startTime, endTime)){
            throw new InvalidReservationPeriodException(startTime, endTime);
        }

        if(!reservationRepository.findConflictingReservations(classroomId, startTime, endTime, id).isEmpty()){
            throw new ClassroomAlreadyReservedAtSpecificPeriodException(
                    classroomId,
                    startTime,
                    endTime);
        }
    }

    public ReservationResponseDTO findById(UUID id) {
        return ReservationResponseDTO.from(
                reservationRepository.findById(id)
                        .orElseThrow(() -> new ReservationNotFoundException(id))
        );
    }

    public ReservationResponseDTO cancel(UUID id) {
        Reservation reservation =  reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        reservation.setStatus(ReservationStatus.CANCELLED);

        return ReservationResponseDTO.from(reservationRepository.save(reservation));
    }

    public List<ReservationResponseDTO> list(UUID classroomId, LocalDateTime startTime, LocalDateTime endTime, ReservationStatus status) {
        return reservationRepository.findWithFilters(classroomId, status, startTime, endTime)
                .stream()
                .map(ReservationResponseDTO::from)
                .toList();
    }
}
