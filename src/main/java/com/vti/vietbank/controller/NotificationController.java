package com.vti.vietbank.controller;

import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.NotificationResponse;
import com.vti.vietbank.repository.UserRepository;
import com.vti.vietbank.service.NotificationService;
import com.vti.vietbank.util.SecurityContextHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "API quản lý thông báo real-time")
@SecurityRequirement(name = "Bearer Authentication")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    @Operation(summary = "Lấy tất cả thông báo", description = "Lấy tất cả thông báo của user hiện tại (cả đã đọc và chưa đọc)")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUserNotifications() {
        String phoneNumber = SecurityContextHelper.getCurrentUsername();
        if (phoneNumber == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not authenticated"));
        }
        Long userId = userRepository.findByPhoneNumber(phoneNumber)
                .map(user -> user.getId())
                .orElse(null);
        if (userId == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/unread")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    @Operation(summary = "Lấy thông báo chưa đọc", description = "Lấy chỉ các thông báo chưa đọc (isRead = false)")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUnreadNotifications() {
        String phoneNumber = SecurityContextHelper.getCurrentUsername();
        if (phoneNumber == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not authenticated"));
        }
        Long userId = userRepository.findByPhoneNumber(phoneNumber)
                .map(user -> user.getId())
                .orElse(null);
        if (userId == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @GetMapping("/read")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    @Operation(summary = "Lấy thông báo đã đọc", description = "Lấy chỉ các thông báo đã đọc (isRead = true)")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getReadNotifications() {
        String phoneNumber = SecurityContextHelper.getCurrentUsername();
        if (phoneNumber == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not authenticated"));
        }
        Long userId = userRepository.findByPhoneNumber(phoneNumber)
                .map(user -> user.getId())
                .orElse(null);
        if (userId == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
        return ResponseEntity.ok(notificationService.getReadNotifications(userId));
    }

    @GetMapping("/unread/count")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    @Operation(summary = "Đếm số thông báo chưa đọc", description = "Trả về số lượng thông báo chưa đọc (dùng cho badge)")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount() {
        String phoneNumber = SecurityContextHelper.getCurrentUsername();
        if (phoneNumber == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not authenticated"));
        }
        Long userId = userRepository.findByPhoneNumber(phoneNumber)
                .map(user -> user.getId())
                .orElse(null);
        if (userId == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    @Operation(summary = "Đánh dấu đã đọc", description = "Đánh dấu một thông báo cụ thể là đã đọc")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Integer id) {
        String phoneNumber = SecurityContextHelper.getCurrentUsername();
        if (phoneNumber == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not authenticated"));
        }
        Long userId = userRepository.findByPhoneNumber(phoneNumber)
                .map(user -> user.getId())
                .orElse(null);
        if (userId == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
        return ResponseEntity.ok(notificationService.markAsRead(id, userId));
    }

    @PutMapping("/read-all")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    @Operation(summary = "Đánh dấu tất cả đã đọc", description = "Đánh dấu tất cả thông báo của user là đã đọc")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead() {
        String phoneNumber = SecurityContextHelper.getCurrentUsername();
        if (phoneNumber == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not authenticated"));
        }
        Long userId = userRepository.findByPhoneNumber(phoneNumber)
                .map(user -> user.getId())
                .orElse(null);
        if (userId == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
        return ResponseEntity.ok(notificationService.markAllAsRead(userId));
    }
}
