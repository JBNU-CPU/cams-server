package com.cpu.cams.activity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
public class RecurringScheduleDTO {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

}
