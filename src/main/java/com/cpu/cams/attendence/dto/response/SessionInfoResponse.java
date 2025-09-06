package com.cpu.cams.attendence.dto.response;

import com.cpu.cams.attendence.entity.Session;
import com.cpu.cams.attendence.entity.SessionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SessionInfoResponse {
    private Long sessionId;
    private Integer sessionNumber;
    private String title;
    private LocalDateTime closedAt;
    private SessionStatus status;
    private String attendancesCode;

    public static SessionInfoResponse from(Session session) {
        return SessionInfoResponse.builder()
                .sessionId(session.getId())
                .sessionNumber(session.getSessionNumber())
                .title(session.getTitle())
                .closedAt(session.getClosedAt())
                .status(session.getStatus())
                .attendancesCode(session.getAttendancesCode())
                .build();
    }
}