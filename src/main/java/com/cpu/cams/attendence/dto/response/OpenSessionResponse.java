package com.cpu.cams.attendence.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OpenSessionResponse {
    private Long sessionId;
    private String activityTitle;
    private Integer sessionNumber; // 회차
}
