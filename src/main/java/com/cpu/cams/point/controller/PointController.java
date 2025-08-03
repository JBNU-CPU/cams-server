package com.cpu.cams.point.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/point")
public class PointController {

    // 내 포인트 조회
    @GetMapping
    public String getPoint() {
        return "OK";
    }

    // 내 포인트 내역 조회
    @GetMapping("/history")
    public String getPointHistory(){
        return "OK";
    }

    // 포인트 랭킹 확인
    @GetMapping("/ranking")
    public String getPointRanking(){
        return "OK";
    }
}
