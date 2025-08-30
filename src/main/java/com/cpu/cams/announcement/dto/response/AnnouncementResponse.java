package com.cpu.cams.announcement.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class AnnouncementResponse {

    private Long id;
    private String title;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt;
}
