package com.cpu.cams.attendence.dto.response;

import com.cpu.cams.attendence.entity.AttendanceStatus;

import java.time.LocalDateTime;

public class ParticipantActivityAttendanceResponse {

    private String activityTitle;
    private Integer sessionNumber;
    private AttendanceStatus attendanceStatus;
    private LocalDateTime attendanceTime;
}
