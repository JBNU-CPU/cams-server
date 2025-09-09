package com.cpu.cams.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileRequest {
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
    @NotBlank(message = "전화번호는 필수입니다.")
    private String phone;
    @NotBlank(message = "학과는 필수입니다.")
    private String department;
    @NotNull(message = "기수는 필수입니다.")
    private Integer cohort;
    private String introduce;
    // TODO: interesting validation 추가 필요
    private List<String> interesting;
}
