package com.cpu.cams.activity.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ParticipantsResponse {
    private String name; // 참가자 이름
    private String email; // 참가자 이메일
    private String phone; // 참가자 번호
    private LocalDateTime joindAt;
}
