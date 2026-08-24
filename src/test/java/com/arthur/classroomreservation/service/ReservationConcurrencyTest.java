package com.arthur.classroomreservation.service;

import com.arthur.classroomreservation.dto.request.ClassroomRequestDTO;
import com.arthur.classroomreservation.dto.request.ReservationRequestDTO;
import com.arthur.classroomreservation.dto.response.ClassroomResponseDTO;
import com.arthur.classroomreservation.entity.enums.ClassroomType;
import com.arthur.classroomreservation.exception.ClassroomAlreadyReservedAtSpecificPeriodException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class ReservationConcurrencyTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClassroomService classroomService;

    @Test
    void onlyOneReservationShouldSucceedWhenMultipleThreadsBookTheSameSlot() throws InterruptedException {
        ClassroomResponseDTO testClassroom = classroomService.create(new ClassroomRequestDTO(
                "3",
                "204",
                30,
                ClassroomType.LABORATORY
        ));

        int threadCount = 10;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger conflictCount = new AtomicInteger(0);

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    latch.countDown();
                    latch.await();

                    ReservationRequestDTO reservationRequestDTO = new ReservationRequestDTO(
                            start,
                            end,
                            testClassroom.id()
                    );

                    reservationService.create(reservationRequestDTO);

                    successCount.incrementAndGet();
                } catch (ClassroomAlreadyReservedAtSpecificPeriodException e) {
                    conflictCount.incrementAndGet();
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(conflictCount.get()).isEqualTo(threadCount - 1);
    }
}