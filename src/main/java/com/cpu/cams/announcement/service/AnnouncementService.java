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

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementService {

    private final MemberRepository memberRepository;
    private final AnnouncementRepository announcementRepository;

    public void createAnnouncement(AnnouncementRequest announcementRequest) {
        // String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = "init2";
        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("멤버없음"));

        Announcement.create(announcementRequest, findMember);
    }

    public Page<AnnouncementResponse> getAnnouncements(int page, int size) {

        Page<AnnouncementResponse> announcements = announcementRepository.findAll(PageRequest.of(page, size)).map(
                announcement -> {
                    return AnnouncementResponse.builder()
                            .id(announcement.getId())
                            .title(announcement.getTitle())
                            .content(announcement.getContent())
                            .createdBy(announcement.getCreatedBy().getName())
                            .createdAt(announcement.getCreatedAt())
                            .build();
                }
        );

        return announcements;
    }

    public AnnouncementResponse getAnnouncement(Long id) {

        Announcement announcement = announcementRepository.findById(id).orElseThrow(() -> new RuntimeException("공지 없음"));
        return AnnouncementResponse.builder()
                .id(announcement.getId())
                .content(announcement.getContent())
                .title(announcement.getTitle())
                .createdAt(announcement.getCreatedAt())
                .createdBy(announcement.getCreatedBy().getName())
                .build();
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
