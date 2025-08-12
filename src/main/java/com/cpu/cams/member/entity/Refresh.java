package com.cpu.cams.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String refresh;
    private String expiration;

    public static Refresh create(String username, String refresh, String expiredMs) {
        Refresh refreshEntity = new Refresh();
        refreshEntity.username = username;
        refreshEntity.refresh = refresh;
        refreshEntity.expiration = expiredMs;
        return refreshEntity;
    }

}
