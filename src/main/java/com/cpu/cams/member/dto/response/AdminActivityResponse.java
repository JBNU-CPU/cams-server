package com.cpu.cams.member.dto.response;

import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.entity.ActivityStatus;
import com.cpu.cams.activity.entity.ActivityType;
import lombok.*;

@Builder @Getter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class AdminActivityResponse {

    private Long id;
    private String studentName;
    private boolean status;
    private String title;
    private ActivityType activityType;
    private String description;
    private String goal;
    private Integer maxParticipants;
    private String location;
    private String notes;

    public static AdminActivityResponse entityToResponse(Activity activity){

        return AdminActivityResponse.builder()
                .id(activity.getId())
                .studentName(activity.getCreatedBy().getName())
                .status(activity.getIsApproved())
                .title(activity.getTitle())
                .activityType(activity.getActivityType())
                .description(activity.getDescription())
                .goal(activity.getGoal())
                .maxParticipants(activity.getMaxParticipants())
                .location(activity.getLocation())
                .notes(activity.getNotes())

                .build();
    }

}
