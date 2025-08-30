package com.cpu.cams.announcement.controller;

import com.cpu.cams.activity.dto.request.ActivityRequest;
import com.cpu.cams.announcement.dto.request.AnnouncementRequest;
import com.cpu.cams.announcement.dto.response.AnnouncementResponse;
import com.cpu.cams.announcement.entity.Announcement;
import com.cpu.cams.announcement.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announcement")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    // 공지 등록
    @PostMapping()
    public void createAnnouncement(@RequestBody AnnouncementRequest announcementRequest) {
        announcementService.createAnnouncement(announcementRequest);
    }

    // 공지 목록 조회
    @GetMapping()
    public Page<AnnouncementResponse> getAnnouncementList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {

        Page<AnnouncementResponse> announcements = announcementService.getAnnouncements(page, size);

        return announcements;
    }

    // 공지 상세 조회
    @GetMapping("/{announcementId}")
    public AnnouncementResponse getAnnouncement(@PathVariable Long announcementId) {
        AnnouncementResponse announcement = announcementService.getAnnouncement(announcementId);
        return announcement;
    }

    // 공지 수정
    @PutMapping("/{announcementId}")
    public void updateAnnouncement(@PathVariable Long announcementId, @RequestBody AnnouncementRequest announcementRequest) {
        announcementService.updateAnnouncement(announcementId, announcementRequest);
    }

    // 공지 삭제
    @DeleteMapping("/{announcementId}")
    public void deleteAnnouncement(@PathVariable Long announcementId) {
        announcementService.deleteAnnouncement(announcementId);
    }
}
