package com.cpu.cams.activity.entity;

import com.cpu.cams.activity.dto.response.EventScheduleDTO;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
public class EventSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Activity activity;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public void setActivity(Activity activity) {
        this.activity = activity;
        activity.getEventSchedules().add(this);
    }
    public static EventSchedule create(EventScheduleDTO eventScheduleDTO, Activity activity) {
        EventSchedule eventSchedule = new EventSchedule();
        eventSchedule.startDateTime = eventScheduleDTO.getStartDateTime();
        eventSchedule.endDateTime = eventScheduleDTO.getEndDateTime();
        eventSchedule.setActivity(activity);
        return eventSchedule;
    }
}
