//package com.cpu.cams.notification;
//
//import java.time.LocalDateTime;
//
//public record NotificationResponse(
//        Long id, String type, String message, String link,
//        LocalDateTime createdAt, LocalDateTime readAt
//) {
//    public static NotificationResponse from(Notification n) {
//        return new NotificationResponse(
//                n.getId(), n.getType(), n.getMessage(), n.getLink(),
//                n.getCreatedAt(), n.getReadAt()
//        );
//    }
//}
