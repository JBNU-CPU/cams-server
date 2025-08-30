package com.cpu.cams.announcement.service;

import com.cpu.cams.announcement.dto.request.AnnouncementRequest;
import com.cpu.cams.announcement.dto.response.AnnouncementResponse;
import com.cpu.cams.announcement.entity.Announcement;
import com.cpu.cams.announcement.repository.AnnouncementRepository;
import com.cpu.cams.member.dto.response.CustomUserDetails;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.entity.Role;
import com.cpu.cams.member.repository.MemberRepository;
import com.cpu.cams.member.service.MemberService;
import com.cpu.cams.notification.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final MemberRepository memberRepository;
    private final AnnouncementRepository announcementRepository;
    private final MemberService memberService;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher eventPublisher;

    // 공지사항 등록
    @Transactional
    public Long createAnnouncement(AnnouncementRequest announcementRequest, CustomUserDetails customUserDetails) {

        // 관리자 체크
        checkAdmin(customUserDetails);

        Member findMember = memberService.findByUsername(customUserDetails.getUsername());

        Announcement announcement = Announcement.create(announcementRequest, findMember);
        announcementRepository.save(announcement);

        // 모든 사용자에게 알림을 보내기 위한 이벤트 발행
        List<Member> allMembers = memberRepository.findAll();
        for (Member member : allMembers) {
            // 이벤트 객체를 만들어 발행
            NotificationEvent event = new NotificationEvent(member.getUsername(), "새로운 공지사항: " + announcement.getTitle());
            eventPublisher.publishEvent(event);
        }

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

        Announcement announcement = findOne(announcementId);

        return AnnouncementResponse.entityToDto(announcement);
    }

    // 공지사항 수정
    public void updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest, CustomUserDetails customUserDetails) {

        checkAdmin(customUserDetails);

        Announcement announcement = findOne(announcementId);
        announcement.updateAnnouncement(announcementRequest);
    }

    // 공지사항 삭제
    public void deleteAnnouncement(Long announcementId, CustomUserDetails customUserDetails) {

        checkAdmin(customUserDetails);

        Announcement announcement = findOne(announcementId);
        announcementRepository.delete(announcement);
    }

    // 관리자인지 확인하는 로직
    private void checkAdmin(CustomUserDetails customUserDetails) {
        boolean isAdmin = customUserDetails.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority())); // 또는 Role.ROLE_ADMIN.name()

        if (!isAdmin) {
            throw new RuntimeException("너 관리자 아니구나?");
        }
    }
    
    // 공지 확인하는 로직
    private Announcement findOne(Long announcementId){
        return announcementRepository.findById(announcementId).orElseThrow(() -> new RuntimeException("공지 없음"));
    }

    // 이벤트 객체 정의 (간단한 record 또는 class)
    public record NotificationEvent(String username, String message) {}
}
