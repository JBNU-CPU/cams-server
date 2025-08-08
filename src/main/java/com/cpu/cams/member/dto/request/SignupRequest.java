package com.cpu.cams.member.dto.request;

import lombok.Getter;

@Getter
public class SignupRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String department;
    private Integer cohort;
}
