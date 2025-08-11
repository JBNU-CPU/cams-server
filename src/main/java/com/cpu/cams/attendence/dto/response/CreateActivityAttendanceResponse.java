package com.cpu.cams.attendence.dto.response;

import com.cpu.cams.activity.entity.ActivityType;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateActivityAttendanceResponse {
    private Long activityId;
    private String activityTitle;
    private Integer totalSessions; // 전체 회차 수
    private ActivityType activityType;
    private List<ParticipantSummary> participants;
    private List<WeeklySummary> weeklySummaries;

    // 참여자 출석 현황
    public class ParticipantSummary{
        private Long memberId;
        private String name;         // 김철수
        private int attendanceCount; // 출석 횟수
        private int lateCount;       // 지각 횟수
        private int absentCount;     // 결석 횟수
        private int totalSessions;   // 총 세션 수
        private int attendanceRate = (attendanceCount + lateCount) * 100 / totalSessions;;  // 출석률 (백분율)
    }

    // 주차별 출석 현황
    private class WeeklySummary{
        private int sessionNumber;   // 1주차 → 1
        private int attendanceCount;
        private int lateCount;
        private int absentCount;
    }
}