package com.arthur.classroomreservation.util;

import java.time.LocalDateTime;

public class DateTimeUtils {
    private DateTimeUtils(){}

    public static boolean isEndAfterStart(LocalDateTime startTime, LocalDateTime endTime){
        return endTime.isAfter(startTime);
    }
}
