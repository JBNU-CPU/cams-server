package com.cpu.cams.activity.dto.response;

import com.cpu.cams.activity.entity.Curriculum;
import com.cpu.cams.activity.entity.EventSchedule;
import com.cpu.cams.activity.entity.RecurringSchedule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class ActivityResponse {

    private Long id;
    private String title;
    private String description;
    private String createdBy;
    private Integer participantCount;
    private Integer maxParticipants;
    private String activityType;
    private String activityStatus;
    private String notes;
    private String goal;
    private Boolean isApproved;
    private Integer sessionCount;
    private List<RecurringScheduleDTO> recurringSchedules;
    private List<EventScheduleDTO> eventSchedules;
    private List<CurriculumDTO> curriculums;


    public static List<RecurringScheduleDTO> convertRecurringSchedules(List<RecurringSchedule> recurringSchedules) {

        List<RecurringScheduleDTO> recurringScheduleDTOS = recurringSchedules.stream().
                map(recurringSchedule -> {
            return new RecurringScheduleDTO(recurringSchedule.getWeekday(),
                    recurringSchedule.getStartTime(), recurringSchedule.getEndTime());
        }).toList();

        return recurringScheduleDTOS;
    }

    public static List<EventScheduleDTO> convertEventSchedules(List<EventSchedule> eventSchedules) {

        List<EventScheduleDTO> eventScheduleDTOS = eventSchedules.stream().
                map(eventSchedule -> {
                    return new EventScheduleDTO(eventSchedule.getStartDateTime(), eventSchedule.getEndDateTime());
                }).toList();
        return eventScheduleDTOS;
    }

    public static List<CurriculumDTO> convertCurriculums(List<Curriculum> curriculums) {

        List<CurriculumDTO> curriculumDTOS = curriculums.stream().
                map(curriculum -> {
                    return new CurriculumDTO(curriculum.getSequence(), curriculum.getTitle(), curriculum.getDescription());
                }).toList();
        return curriculumDTOS;
    }

}
