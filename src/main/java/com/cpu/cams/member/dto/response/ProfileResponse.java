package com.cpu.cams.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private Integer cohort;
    private Integer totalPoints;
    private String introduce;
}
