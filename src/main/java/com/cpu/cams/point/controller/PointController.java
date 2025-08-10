package com.cpu.cams.point.controller;

import com.cpu.cams.point.dto.response.PointHistoryResponse;
import com.cpu.cams.point.dto.response.PointRankingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/point")
public class PointController {

    // 내 포인트 조회
    @GetMapping
    public Integer getPoint() {
        return 1000;
    }

    // 내 포인트 내역 조회
    @GetMapping("/history")
    public List<PointHistoryResponse> getPointHistory(){
        return List.of(new PointHistoryResponse());
    }

    // 포인트 랭킹 확인
    @GetMapping("/ranking")
    public List<PointRankingResponse> getPointRanking(){
        return List.of(new PointRankingResponse());
    }
}
