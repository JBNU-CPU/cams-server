package com.cpu.cams.attendence.dto.response;

import com.cpu.cams.attendence.entity.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionAttendanceResponse {
    private Long attendanceId;
    private String attendanceStatus;
    private String participantId;
    private String participantName;
    private String participantDepartment;
}
