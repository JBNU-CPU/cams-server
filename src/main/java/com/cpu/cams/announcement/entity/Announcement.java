package com.cpu.cams.announcement.entity;

import com.cpu.cams.announcement.dto.request.AnnouncementRequest;
import com.cpu.cams.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Member createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static Announcement create(AnnouncementRequest announcementRequest, Member member) {
        Announcement announcement = new Announcement();
        announcement.title = announcementRequest.getTitle();
        announcement.content = announcementRequest.getContent();
        announcement.addCreatedBy(member);
        return announcement;
    }

    private void addCreatedBy(Member member) {
        this.createdBy = member;
        member.getAnnouncementList().add(this);
    }

    public void updateAnnouncement(AnnouncementRequest announcementRequest) {
        this.title = announcementRequest.getTitle();
        this.content = announcementRequest.getContent();
    }
}
