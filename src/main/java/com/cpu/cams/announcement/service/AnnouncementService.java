package com.cpu.cams.announcement.service;

import com.cpu.cams.announcement.dto.request.AnnouncementRequest;
import com.cpu.cams.announcement.dto.response.AnnouncementResponse;
import com.cpu.cams.announcement.entity.Announcement;
import com.cpu.cams.announcement.repository.AnnouncementRepository;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementService {

    private final MemberRepository memberRepository;
    private final AnnouncementRepository announcementRepository;

    // 공지사항 등록
    public Long createAnnouncement(AnnouncementRequest announcementRequest, String username) {

        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("멤버없음"));

        Announcement announcement = Announcement.create(announcementRequest, findMember);
        return announcement.getId();
    }

    // 공지사항 전체 조회
    public Page<AnnouncementResponse> getAnnouncements(int page, int size) {

        return announcementRepository.findAll(PageRequest.of(page, size)).map(
                announcement -> {
                    return AnnouncementResponse.entityToDto(announcement);
                }
        );
    }

    // 공지사항 세부 조회
    public AnnouncementResponse getAnnouncementDetail(Long announcementId) {

        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new RuntimeException("공지 없음"));

        return AnnouncementResponse.entityToDto(announcement);
    }

    public void updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest) {

        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new RuntimeException("공지 없음"));
        announcement.updateAnnouncement(announcementRequest);
    }

    public void deleteAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new RuntimeException("공지 없음"));
        announcementRepository.delete(announcement);
    }
}
