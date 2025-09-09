package com.cpu.cams.attendence.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionRequest {
    @NotBlank(message = "출석 코드는 필수입니다.")
    @Size(min = 4, max = 4, message = "출석 코드는 4자리여야 합니다.")
    private String attendanceCode;
    @NotNull(message = "출석 가능 시간은 필수입니다.")
    private Integer closableAfterMinutes; // 출석 가능 시간
}
