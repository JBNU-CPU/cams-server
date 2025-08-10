package com.cpu.cams.activity.entity;

import com.cpu.cams.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_participants_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @CreationTimestamp
    private LocalDateTime joinedAt;

    public void addParticipantAssociation(Member member, Activity activity) {
        this.activity = activity;
        this.member = member;
        member.getParticipatedActivities().add(this);
        activity.getParticipants().add(this);
    }
    public static ActivityParticipant create(Activity activity, Member member) {
        ActivityParticipant participant = new ActivityParticipant();
        participant.addParticipantAssociation(member, activity);

        return participant;
    }
}
