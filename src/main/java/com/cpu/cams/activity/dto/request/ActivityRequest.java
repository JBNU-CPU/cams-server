package com.cpu.cams.activity.dto.request;

import com.cpu.cams.activity.dto.response.CurriculumDTO;
import com.cpu.cams.activity.dto.response.EventScheduleDTO;
import com.cpu.cams.activity.dto.response.RecurringScheduleDTO;
import com.cpu.cams.activity.entity.ActivityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRequest {

    @NotBlank(message = "활동 제목은 필수입니다.")
    private String title;
    @NotBlank(message = "활동 설명은 필수입니다.")
    private String description;
    private String goal;
    @NotNull(message = "활동 타입은 필수입니다.")
    private ActivityType activityType;
    @NotNull(message = "최대 참여 인원은 필수입니다.")
    private Integer maxParticipants;
    @NotBlank(message = "장소 설명은 필수입니다.")
    private String location;
    private String notes;
    // TODO: recurringSchedules, eventSchedule, curriculums validation 추가 필요
    private List<RecurringScheduleDTO> recurringSchedules;
    private List<EventScheduleDTO> eventSchedule;
    private List<CurriculumDTO> curriculums;
}
