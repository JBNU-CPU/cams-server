package com.cpu.cams.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

    @NotBlank(message = "학번은 필수입니다.")
    private String username;
    @NotBlank(message = "새로운 비밀번호는 필수입니다.")
    private String password;
}
