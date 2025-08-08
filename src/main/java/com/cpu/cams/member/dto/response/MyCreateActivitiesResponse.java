package com.cpu.cams.member.dto.response;

import com.cpu.cams.activity.dto.response.ParticipantsResponse;
import com.cpu.cams.activity.entity.ActivityStatus;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class MyCreateActivitiesResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private Integer participantCount;
    private Integer maxParticipants;
    // recurringSchedules 또는 eventSchedules 하나만 넘어가도록
    private List<RecurringScheduleDTO> recurringSchedules;
    private List<EventScheduleDTO> eventSchedules;
    private List<ParticipantsResponse> participantes;
    private ActivityStatus activityStatus;

    private class RecurringScheduleDTO {
        private DayOfWeek dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;
    }

    private class EventScheduleDTO {
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
    }

}

