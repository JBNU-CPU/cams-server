package com.cpu.cams.point.service;

import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import com.cpu.cams.member.service.MemberService;
import com.cpu.cams.point.dto.response.PointHistoryResponse;
import com.cpu.cams.point.repository.PointRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PointService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    // 포인트 전체 내역 조회하기
    public List<PointHistoryResponse> getPointHistory(String username) {
        Member findMember = memberService.findByUsername(username);
        List<PointHistoryResponse> result = findMember.getPointList().stream().map(point -> {
            return PointHistoryResponse.builder()
                    .type(point.getType())
                    .amount(point.getAmount())
                    .description(point.getDescription())
                    .createdAt(point.getCreatedAt())
                    .build();
        }).toList();

        return result;
    }
}
