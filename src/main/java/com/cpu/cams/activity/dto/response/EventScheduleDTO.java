package com.cpu.cams.activity.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EventScheduleDTO {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
