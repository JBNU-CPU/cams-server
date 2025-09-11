package com.cpu.cams.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class EmailAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String authCode;

    public EmailAuth(String email, String authCode){
        this.email = email;
        this.authCode = authCode;
    }

    public void patch(String authCode){
        this.authCode = authCode;
    }
}
