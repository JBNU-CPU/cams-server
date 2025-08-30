package com.cpu.cams.point.controller;

import com.cpu.cams.point.dto.response.PointHistoryResponse;
import com.cpu.cams.point.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    // 내 포인트 내역 조회
    @GetMapping("/history")
    public ResponseEntity<List<PointHistoryResponse>> getPointHistory(@AuthenticationPrincipal UserDetails userDetails) {

        List<PointHistoryResponse> pointHistories = pointService.getPointHistory(userDetails.getUsername());
        return ResponseEntity.ok(pointHistories);
    }

    // todo: 포인트 랭킹 확인
//    @GetMapping("/ranking")
//    public List<PointRankingResponse> getPointRanking() {
//        return List.of(new PointRankingResponse());
//    }

}
