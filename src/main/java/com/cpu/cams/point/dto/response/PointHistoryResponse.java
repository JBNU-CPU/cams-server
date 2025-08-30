package com.cpu.cams.point.dto.response;

import com.cpu.cams.point.entity.PointType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class PointHistoryResponse {
    PointType type;
    String description;
    Integer amount;
    LocalDateTime createdAt;
}
