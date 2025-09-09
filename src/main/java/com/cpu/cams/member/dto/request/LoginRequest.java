package com.cpu.cams.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "이름은 필수입니다.")
    private String username;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
