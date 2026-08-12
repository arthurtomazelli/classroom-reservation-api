package com.arthur.classroomreservation.repository;

import com.arthur.classroomreservation.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClassroomRepository extends JpaRepository<Classroom, UUID>{
    boolean existsByBlockAndNumber(String block, String number);
    Optional<Classroom> findByBlockAndNumber(String block, String number);

    @Query("SELECT c FROM Classroom c WHERE :available IS NULL OR c.available = :available")
    List<Classroom> findByAvailableOptional(@Param("available") Boolean available);

    @Query("SELECT c FROM Classroom c " +
            "WHERE c.available = true " +
            "AND NOT EXISTS (" +
            "    SELECT r FROM Reservation r " +
            "    WHERE r.classroom = c " +
            "    AND r.status <> 'CANCELLED' " +
            "    AND r.startTime < :end " +
            "    AND r.endTime > :start" +
            ")")
    List<Classroom> findFreeForReservation(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
