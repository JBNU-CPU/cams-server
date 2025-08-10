package com.cpu.cams.activity.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CurriculumDTO {
    Integer sequence;
    String title;
    String description;
}
