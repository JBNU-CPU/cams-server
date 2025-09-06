package com.cpu.cams.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupRequest {
    private String username;
    private String password;
    private String name;
//    private String email;
//    private String phone;
//    private String department;
//    private Integer cohort;
}
