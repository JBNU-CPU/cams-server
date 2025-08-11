package com.cpu.cams.member.dto.response;

import com.cpu.cams.activity.dto.response.EventScheduleDTO;
import com.cpu.cams.activity.dto.response.ParticipantResponse;
import com.cpu.cams.activity.dto.response.RecurringScheduleDTO;
import com.cpu.cams.activity.entity.ActivityStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MyParticipantsActivitiesResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Integer participantCount;
    private Integer maxParticipants;
    private ActivityStatus activityStatus;
    private String leaderName;

    // recurringSchedules 또는 eventSchedules 하나만 넘어가도록
    private List<RecurringScheduleDTO> recurringSchedules;
    private List<EventScheduleDTO> eventSchedules;
    private List<ParticipantResponse> participants;
}
