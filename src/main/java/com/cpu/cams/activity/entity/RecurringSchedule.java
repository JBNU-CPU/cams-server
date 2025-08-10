package com.cpu.cams.activity.entity;

import com.cpu.cams.activity.dto.response.RecurringScheduleDTO;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Entity @Getter
public class RecurringSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recurring_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Activity activity;

    private DayOfWeek weekday;
    private LocalTime startTime;
    private LocalTime endTime;

    public void setAcitivity(Activity activity) {
        this.activity = activity;
        activity.getRecurringSchedules().add(this);
    }

    public static RecurringSchedule create(RecurringScheduleDTO recurringScheduleDTO, Activity activity) {
        RecurringSchedule recurringSchedule = new RecurringSchedule();
        recurringSchedule.weekday = recurringScheduleDTO.getDayOfWeek();
        recurringSchedule.startTime = recurringScheduleDTO.getStartTime();
        recurringSchedule.endTime = recurringScheduleDTO.getEndTime();
        recurringSchedule.setAcitivity(activity);
        return recurringSchedule;
    }

    public RecurringSchedule update(RecurringScheduleDTO recurringScheduleDTO) {
        this.weekday = recurringScheduleDTO.getDayOfWeek();
        this.startTime = recurringScheduleDTO.getStartTime();
        this.endTime = recurringScheduleDTO.getEndTime();
        return this;
    }

}
