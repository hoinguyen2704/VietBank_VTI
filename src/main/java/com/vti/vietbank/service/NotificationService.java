package com.vti.vietbank.service;

import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    void sendTransferNotification(Long fromUserId, Long toUserId,
            String fromAccountNumber, String toAccountNumber,
            String amount, String transactionCode, String description);

    ApiResponse<List<NotificationResponse>> getUserNotifications(Long userId);

    ApiResponse<List<NotificationResponse>> getUnreadNotifications(Long userId);

    ApiResponse<List<NotificationResponse>> getReadNotifications(Long userId);

    ApiResponse<Void> markAsRead(Integer notificationId, Long userId);

    ApiResponse<Void> markAllAsRead(Long userId);

    ApiResponse<Long> getUnreadCount(Long userId);
}
