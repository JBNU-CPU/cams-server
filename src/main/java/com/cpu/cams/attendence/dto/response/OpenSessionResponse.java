package com.cpu.cams.attendence.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class OpenSessionResponse {
    private Long sessionId;
    private String activityTitle;
    private Integer sessionNumber; // 회차
    private LocalDateTime closedAt; // 마감 시간
}
