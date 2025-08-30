package com.cpu.cams.announcement.controller;

import com.cpu.cams.announcement.dto.request.AnnouncementRequest;
import com.cpu.cams.announcement.dto.response.AnnouncementResponse;
import com.cpu.cams.announcement.service.AnnouncementService;
import com.cpu.cams.member.dto.response.CustomUserDetails;
import com.cpu.cams.member.entity.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announcement")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    // 공지 등록
    @PostMapping()
    public ResponseEntity<Long> createAnnouncement(
            @RequestBody AnnouncementRequest announcementRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        
        Long announcement = announcementService.createAnnouncement(announcementRequest, customUserDetails);
        return ResponseEntity.ok(announcement);
    }

    // 공지 목록 조회
    @GetMapping()
    public ResponseEntity<Page<AnnouncementResponse>> getAnnouncementList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Page<AnnouncementResponse> announcements = announcementService.getAnnouncements(page, size);

        return ResponseEntity.ok(announcements);
    }

    // 공지 상세 조회
    @GetMapping("/{announcementId}")
    public ResponseEntity<AnnouncementResponse> getAnnouncement(@PathVariable Long announcementId) {
        AnnouncementResponse announcement = announcementService.getAnnouncementDetail(announcementId);

        return ResponseEntity.ok(announcement);
    }

    // 공지 수정
    @PutMapping("/{announcementId}")
    public void updateAnnouncement(
            @PathVariable Long announcementId,
            @RequestBody AnnouncementRequest announcementRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        announcementService.updateAnnouncement(announcementId, announcementRequest, customUserDetails);
    }

    // 공지 삭제
    @DeleteMapping("/{announcementId}")
    public void deleteAnnouncement(@PathVariable Long announcementId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        announcementService.deleteAnnouncement(announcementId, customUserDetails);
    }
}
