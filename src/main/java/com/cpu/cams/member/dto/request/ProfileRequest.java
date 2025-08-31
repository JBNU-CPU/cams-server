package com.cpu.cams.member.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileRequest {
    private String name;
    private String email;
    private String phone;
    private String department;
    private Integer cohort;
    private String introduce;
    private List<String> interesting;
}
