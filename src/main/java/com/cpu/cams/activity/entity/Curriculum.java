package com.cpu.cams.activity.entity;

import com.cpu.cams.activity.dto.response.CurriculumDTO;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Curriculum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curriculum_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    private Integer sequence; // 주차 혹은 식순

    private String title;

    private String description;

    // 연관관계 편의 메서드
    public void setActivity(Activity activity) {
        this.activity = activity;
        activity.getCurriculums().add(this);
    }

    public static Curriculum create(CurriculumDTO curriculumDTO, Activity activity) {
        Curriculum curriculum = new Curriculum();
        curriculum.sequence = curriculumDTO.getSequence();
        curriculum.title = curriculumDTO.getTitle();
        curriculum.description = curriculumDTO.getDescription();
        curriculum.setActivity(activity);
        return curriculum;
    }

    public Curriculum update(CurriculumDTO curriculumDTO) {
        this.sequence = curriculumDTO.getSequence();
        this.title = curriculumDTO.getTitle();
        this.description = curriculumDTO.getDescription();
        return this;
    }
}
