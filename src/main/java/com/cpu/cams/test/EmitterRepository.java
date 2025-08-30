package com.cpu.cams.test;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {
    // userId -> (emitterId -> emitter)
    private final Map<Long, Map<String, SseEmitter>> store = new ConcurrentHashMap<>();

    public void save(Long userId, String emitterId, SseEmitter emitter) {
        store.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()).put(emitterId, emitter);
    }

    public Map<String, SseEmitter> getAll(Long userId) {
        return store.getOrDefault(userId, Map.of());
    }

    public void remove(Long userId, String emitterId) {
        Map<String, SseEmitter> map = store.get(userId);
        if (map != null) {
            map.remove(emitterId);
            if (map.isEmpty()) store.remove(userId);
        }
    }

    public void removeAll(Long userId) {
        store.remove(userId);
    }

    public Set<Long> userIds() {
        return new HashSet<>(store.keySet()); // 스냅샷
    }
}
