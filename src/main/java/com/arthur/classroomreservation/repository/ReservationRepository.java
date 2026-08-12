package com.arthur.classroomreservation.repository;

import com.arthur.classroomreservation.entity.Reservation;
import com.arthur.classroomreservation.entity.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.classroom.id = :classroomId " +
            "AND r.status <> 'CANCELLED' " +
            "AND r.startTime < :end " +
            "AND r.endTime > :start " +
            "AND (:excludeId IS NULL OR r.id <> :excludeId)")
    List<Reservation> findConflictingReservations(
            @Param("classroomId") UUID classroomId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("excludeId") UUID excludeId
    );

    @Query("SELECT r FROM Reservation r " +
            "WHERE (:classroomId IS NULL OR r.classroom.id = :classroomId) " +
            "AND (:status IS NULL OR r.status = :status) " +
            "AND (:startTime IS NULL OR r.endTime > :startTime) " +
            "AND (:endTime IS NULL OR r.startTime < :endTime)")
    List<Reservation> findWithFilters(
            @Param("classroomId") UUID classroomId,
            @Param("status") ReservationStatus status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );


}
