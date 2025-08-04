package com.cpu.cams.activity.dto.request;

import com.cpu.cams.activity.entity.ActivityType;
import lombok.Getter;

import java.util.Map;

@Getter
public class ActivityRequest {

    private String title;
    private String description;
    private String goal;
    private ActivityType activityType;
    private Integer maxParticipants;
    private String location;
    private String notes;
    private Map<String, Object> recurringSchedule;
    private Map<String, Object> eventSchedule;
    private Map<String, Object> curriculums;
}
