package com.cpu.cams.point.service;

import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import com.cpu.cams.point.dto.response.PointHistoryResponse;
import com.cpu.cams.point.repository.PointRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;

    public List<PointHistoryResponse> getPointHistory() {
        // String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = "init2";
        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("멤버없음"));
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
