package com.cpu.cams.announcement.service;

import com.cpu.cams.announcement.dto.request.AnnouncementRequest;
import com.cpu.cams.announcement.dto.response.AnnouncementResponse;
import com.cpu.cams.announcement.entity.Announcement;
import com.cpu.cams.announcement.repository.AnnouncementRepository;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.entity.Role;
import com.cpu.cams.member.repository.MemberRepository;
import com.cpu.cams.member.service.MemberService;
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
    private final MemberService memberService;

    // 공지사항 등록
    public Long createAnnouncement(AnnouncementRequest announcementRequest, String username) {

        Member findMember = memberService.findByUsername(username);
        isAdmin(findMember);

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

        Announcement announcement = findOne(announcementId);

        return AnnouncementResponse.entityToDto(announcement);
    }

    // 공지사항 수정
    public void updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest, String username) {

        Member findMember = memberService.findByUsername(username);
        isAdmin(findMember);

        Announcement announcement = findOne(announcementId);
        announcement.updateAnnouncement(announcementRequest);
    }

    // 공지사항 삭제
    public void deleteAnnouncement(Long announcementId, String username) {

        Member findMember = memberService.findByUsername(username);
        isAdmin(findMember);

        Announcement announcement = findOne(announcementId);
        announcementRepository.delete(announcement);
    }

    // 관리자인지 확인하는 로직
    private void isAdmin(Member findMember) {
        if(!findMember.getRole().equals(Role.ROLE_ADMIN)){
            throw new RuntimeException("너 관리자 맞아?");
        }
    }
    
    // 공지 확인하는 로직
    private Announcement findOne(Long announcementId){
        return announcementRepository.findById(announcementId).orElseThrow(() -> new RuntimeException("공지 없음"));
    }
}
