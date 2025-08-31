package com.cpu.cams.activity.entity;

import com.cpu.cams.attendence.entity.Attendance;
import com.cpu.cams.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_participants_id")
    private Long id;

    @CreationTimestamp
    private LocalDateTime joinedAt;

    // == 연관관계 == //
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private List<Attendance> attendances = new ArrayList<>();


    // == 연관관계 편의 메서드 == //
    public void addParticipantAssociation(Member member, Activity activity) {
        this.activity = activity;
        this.member = member;
        member.getParticipatedActivities().add(this);
        activity.getParticipants().add(this);
        activity.addParticipant();
    }

    // == 생성 메서드 == //
    public static ActivityParticipant create(Activity activity, Member member) {
        ActivityParticipant participant = new ActivityParticipant();
        participant.addParticipantAssociation(member, activity);

        return participant;
    }

    // == 비즈니스 로직 == //


}
