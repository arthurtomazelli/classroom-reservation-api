package com.arthur.classroomreservation.repository;

import com.arthur.classroomreservation.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClassroomRepository extends JpaRepository<Classroom, UUID>{
    boolean existsByBlockAndNumber(String block, String number);
    Optional<Classroom> findByBlockAndNumber(String block, String number);

    @Query("SELECT c FROM Classroom c WHERE :available IS NULL OR c.available = :available")
    List<Classroom> findByAvailableOptional(@Param("available") Boolean available);
}
