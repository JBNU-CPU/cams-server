package com.cpu.cams;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private Integer status;
    private String message;
    private T data;
}
