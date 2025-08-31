package com.cpu.cams.attendence.dto.request;

import lombok.Getter;

@Getter
public class SessionRequest {
    private String title;
    private Integer sessionNumber;
    private String attendanceCode;
    private Integer closableAfterMinutes; // 출석 가능 시간
}
