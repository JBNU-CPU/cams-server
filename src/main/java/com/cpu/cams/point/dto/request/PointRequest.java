package com.cpu.cams.point.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PointRequest {

    private String type;
    private String description;
    private Integer amount;

}
