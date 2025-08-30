package com.cpu.cams.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository { // 현재 어떤 사용자가 온라인 상태인지 실시간으로 연결 정보를 관리하는 접수 데스크 역할

    // 여러 서버 인스턴스 환경을 고려한다면 Redis 같은 외부 저장소 사용
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>(); 

    public void save(String username, SseEmitter emitter) {
        emitters.put(username, emitter);
    }

    public void deleteByUsername(String username) {
        emitters.remove(username);
    }

    public SseEmitter get(String username) {
        return emitters.get(username);
    }
}
