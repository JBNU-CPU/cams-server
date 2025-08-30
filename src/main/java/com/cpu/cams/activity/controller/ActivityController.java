package com.cpu.cams.activity.controller;

import com.cpu.cams.ResponseDto;
import com.cpu.cams.activity.dto.request.ActivityRequest;
import com.cpu.cams.activity.dto.response.ActivityResponse;
import com.cpu.cams.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    // 개설하기
    @PostMapping
    public String createActivity(@RequestBody ActivityRequest activityRequest) {
        // todo: 리다이렉트용 URI 보내주기
        activityService.createActivity(activityRequest);
        return "OK";
    }
    
    // 목록조회하기
    @GetMapping
    public ResponseEntity<Page<ActivityResponse>> getActivities(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {

        Page<ActivityResponse> activities = activityService.getActivities(page, size);
        return ResponseEntity.ok().body(activities);
    }

    // 개별조회하기
    @GetMapping("/{activityId}")
    public ResponseEntity<?> getActivity(@PathVariable Long activityId) {

        ActivityResponse activity = activityService.getActivity(activityId);
        return ResponseEntity.ok().body(activity);
    }

     //활동수정하기
    @PutMapping("/{activityId}")
    public ResponseEntity<Long> updateActivity(@PathVariable Long activityId, @RequestBody ActivityRequest activityRequest) {
        Long updateActivityId = activityService.updateActivity(activityId, activityRequest);
        return ResponseEntity.ok().body(updateActivityId);
    }

    // 활동 상태 변경 (모집 중, 마감)
    @PutMapping("/{activityId}/status")
    public ResponseEntity<String> updateActivityStatus(@PathVariable Long activityId, @RequestParam String status) {
        String updateActivityStatus = activityService.updateStatus(activityId, status);
        return ResponseEntity.ok().body(updateActivityStatus);
    }

    // 활동 삭제하기
    @DeleteMapping("/{activityId}")
    public ResponseEntity<Long> deleteActivity(@PathVariable Long activityId) {
        Long deletedActivityId = activityService.deleteActivity(activityId);
        return ResponseEntity.ok().body(deletedActivityId);
    }

    // 검색
    @GetMapping("/search")
    public ResponseEntity<Page<ActivityResponse>> searchActivity(@RequestParam String type, @RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<ActivityResponse> activities = activityService.searchActivity(type, keyword, page, size);
        return ResponseEntity.ok().body(activities);
    }
}
