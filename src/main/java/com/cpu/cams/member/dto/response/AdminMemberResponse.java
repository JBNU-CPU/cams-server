package com.cpu.cams.member.dto.response;

import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.entity.Role;
import lombok.*;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminMemberResponse {
    private Long id;
    private String name;
    private String email;
    private String studentId;
    private Role role;

    public static AdminMemberResponse entityToResponse(Member member){

        return AdminMemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .studentId(member.getUsername())
                .role(member.getRole())
                .build();
    }
}
