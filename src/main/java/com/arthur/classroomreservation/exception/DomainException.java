package com.arthur.classroomreservation.exception;

public sealed interface DomainException permits
        ClassroomAlreadyExistsException,
        ClassroomNotFoundException,
        ClassroomAlreadyReservedAtSpecificPeriodException,
        InvalidReservationPeriodException,
        ReservationNotFoundException
{}
