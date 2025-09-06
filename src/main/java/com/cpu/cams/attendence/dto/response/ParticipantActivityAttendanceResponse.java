package com.cpu.cams.attendence.dto.response;

import com.cpu.cams.attendence.entity.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ParticipantActivityAttendanceResponse {
    private Long attendanceId;
    private Long activityId;
    private String activityTitle;
    private Integer sessionNumber;
    private AttendanceStatus attendanceStatus;
    private LocalDateTime attendanceTime;
}
