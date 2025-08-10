package com.cpu.cams.point.dto.response;

import com.cpu.cams.point.entity.PointType;

import java.time.LocalDateTime;

public class PointHistoryResponse {
    PointType type;
    String description;
    Integer amount;
    LocalDateTime createdAt;
}
