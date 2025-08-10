package com.cpu.cams.activity.dto.request;

import com.cpu.cams.activity.dto.response.CurriculumDTO;
import com.cpu.cams.activity.dto.response.EventScheduleDTO;
import com.cpu.cams.activity.dto.response.RecurringScheduleDTO;
import com.cpu.cams.activity.entity.ActivityType;
import lombok.Getter;

import java.util.List;

@Getter
public class ActivityRequest {

    private String title;
    private String description;
    private String goal;
    private ActivityType activityType;
    private Integer maxParticipants;
    private String location;
    private String notes;
    private List<RecurringScheduleDTO> recurringSchedules;
    private List<EventScheduleDTO> eventSchedule;
    private List<CurriculumDTO> curriculums;
}
