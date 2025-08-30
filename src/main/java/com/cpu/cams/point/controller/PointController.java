package com.cpu.cams.point.controller;

import com.cpu.cams.point.dto.response.PointHistoryResponse;
import com.cpu.cams.point.dto.response.PointRankingResponse;
import com.cpu.cams.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    // 내 포인트 내역 조회
    @GetMapping("/history")
    public List<PointHistoryResponse> getPointHistory() {
        List<PointHistoryResponse> pointHistories = pointService.getPointHistory();
        return pointHistories;
    }

    // todo: 포인트 랭킹 확인
//    @GetMapping("/ranking")
//    public List<PointRankingResponse> getPointRanking() {
//        return List.of(new PointRankingResponse());
//    }

}
