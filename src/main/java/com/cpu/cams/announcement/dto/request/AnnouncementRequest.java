package com.cpu.cams.announcement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementRequest {

    @NotBlank(message = "공지 제목은 필수입니다.")
    private String title;
    @NotBlank(message = "공지 내용은 필수입니다.")
    private String content;

}
