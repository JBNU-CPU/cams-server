package com.cpu.cams.activity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
public class EventScheduleDTO {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
