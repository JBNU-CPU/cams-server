package com.cpu.cams.activity.dto.request;

import com.cpu.cams.activity.entity.ActivityType;
import com.cpu.cams.activity.entity.RecurringSchedule;

import java.util.Map;
import java.util.Objects;

public class ActivityRequest {

    private String title;
    private String description;
    private String goal;
    private ActivityType activityType;
    private Integer maxParticipants;
    private String location;
    private String notes;
    private Map<String, Objects> recurringSchedule;
    private Map<String, Objects> eventSchedule;
    private Map<String, Objects> curriculums;
}
