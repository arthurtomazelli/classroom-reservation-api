package com.arthur.classroomreservation.repository;

import com.arthur.classroomreservation.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClassroomRepository extends JpaRepository<Classroom, UUID>{

}
