package com.cpu.cams.member.dto.response;

import com.cpu.cams.member.entity.Interesting;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    private List<Interesting> interesting;
}
