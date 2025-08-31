package com.cpu.cams.attendence.dto.request;

import lombok.Getter;

@Getter
public class SessionRequest {
    private Integer sessionNumber;
    private String description;
    private String attendanceCode;
    private Integer closableAfterMinutes; // 출석 가능 시간
}
