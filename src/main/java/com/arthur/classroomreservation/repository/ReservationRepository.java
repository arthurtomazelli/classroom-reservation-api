package com.arthur.classroomreservation.repository;

import com.arthur.classroomreservation.entity.Reservation;
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


}
