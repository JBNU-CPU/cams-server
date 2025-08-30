package com.cpu.cams.announcement.dto.response;

import com.cpu.cams.announcement.entity.Announcement;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnnouncementResponse {

    private Long id;
    private String title;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt;

    public static AnnouncementResponse entityToDto(Announcement announcement){
        return AnnouncementResponse.builder()
                .id(announcement.getId())
                .content(announcement.getContent())
                .title(announcement.getTitle())
                .createdAt(announcement.getCreatedAt())
                .createdBy(announcement.getCreatedBy().getName())
                .build();
    }
}
