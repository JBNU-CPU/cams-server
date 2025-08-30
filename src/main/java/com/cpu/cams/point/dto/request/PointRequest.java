package com.cpu.cams.point.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointRequest {

    private String type;
    private String description;
    private Integer amount;

}
