package com.vti.vietbank.service.impl;

import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.NotificationResponse;
import com.vti.vietbank.entity.Notification;
import com.vti.vietbank.entity.User;
import com.vti.vietbank.repository.NotificationRepository;
import com.vti.vietbank.repository.UserRepository;
import com.vti.vietbank.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

        private final NotificationRepository notificationRepository;
        private final UserRepository userRepository;
        private final SimpMessagingTemplate messagingTemplate;

        @Override
        @Transactional
        public void sendTransferNotification(Long fromUserId, Long toUserId,
                        String fromAccountNumber, String toAccountNumber,
                        String amount, String transactionCode, String description) {

                // Format amount for display
                NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
                String formattedAmount = formatter.format(new BigDecimal(amount));

                // Get users
                User fromUser = userRepository.findById(fromUserId)
                                .orElseThrow(() -> new RuntimeException("User not found: " + fromUserId));
                User toUser = userRepository.findById(toUserId)
                                .orElseThrow(() -> new RuntimeException("User not found: " + toUserId));

                // Create notification for sender
                Notification senderNotification = new Notification();
                senderNotification.setUser(fromUser);
                senderNotification.setTitle("Chuyển tiền thành công");
                senderNotification.setMessage(String.format(
                                "Bạn đã chuyển %s đến tài khoản %s. Mã giao dịch: %s",
                                formattedAmount, toAccountNumber, transactionCode));
                senderNotification.setType("TRANSFER_SENT");
                senderNotification.setIsRead(false);
                senderNotification = notificationRepository.save(senderNotification);

                // Create notification for receiver
                Notification receiverNotification = new Notification();
                receiverNotification.setUser(toUser);
                receiverNotification.setTitle("Nhận tiền thành công");
                receiverNotification.setMessage(String.format(
                                "Bạn đã nhận %s từ tài khoản %s. Mã giao dịch: %s",
                                formattedAmount, fromAccountNumber, transactionCode));
                receiverNotification.setType("TRANSFER_RECEIVED");
                receiverNotification.setIsRead(false);
                receiverNotification = notificationRepository.save(receiverNotification);

                // Send real-time notification via WebSocket
                NotificationResponse senderResponse = convertToResponse(senderNotification);
                NotificationResponse receiverResponse = convertToResponse(receiverNotification);

                // Send to sender
                messagingTemplate.convertAndSendToUser(
                                fromUser.getPhoneNumber(),
                                "/queue/notifications",
                                senderResponse);

                // Send to receiver
                messagingTemplate.convertAndSendToUser(
                                toUser.getPhoneNumber(),
                                "/queue/notifications",
                                receiverResponse);
        }

        @Override
        public ApiResponse<List<NotificationResponse>> getUserNotifications(Long userId) {
                List<Notification> notifications = notificationRepository.findByUser_IdOrderByCreatedAtDesc(userId);
                List<NotificationResponse> responses = notifications.stream()
                                .map(this::convertToResponse)
                                .toList();
                return ApiResponse.success(responses);
        }

        @Override
        public ApiResponse<List<NotificationResponse>> getUnreadNotifications(Long userId) {
                List<Notification> notifications = notificationRepository
                                .findByUser_IdAndIsReadFalseOrderByCreatedAtDesc(userId);
                List<NotificationResponse> responses = notifications.stream()
                                .map(this::convertToResponse)
                                .toList();
                return ApiResponse.success(responses);
        }

        @Override
        public ApiResponse<List<NotificationResponse>> getReadNotifications(Long userId) {
                List<Notification> notifications = notificationRepository
                                .findByUser_IdAndIsReadTrueOrderByCreatedAtDesc(userId);
                List<NotificationResponse> responses = notifications.stream()
                                .map(this::convertToResponse)
                                .toList();
                return ApiResponse.success(responses);
        }

        @Override
        @Transactional
        public ApiResponse<Void> markAsRead(Integer notificationId, Long userId) {
                Notification notification = notificationRepository.findById(notificationId)
                                .orElseThrow(() -> new RuntimeException("Notification not found"));

                // Verify ownership
                if (notification.getUser().getId() != userId) {
                        throw new RuntimeException("Unauthorized to mark this notification as read");
                }

                notificationRepository.markAsRead(notificationId);
                return ApiResponse.success(null);
        }

        @Override
        @Transactional
        public ApiResponse<Void> markAllAsRead(Long userId) {
                notificationRepository.markAllAsReadByUserId(userId);
                return ApiResponse.success(null);
        }

        @Override
        public ApiResponse<Long> getUnreadCount(Long userId) {
                long count = notificationRepository.countByUser_IdAndIsReadFalse(userId);
                return ApiResponse.success(count);
        }

        private NotificationResponse convertToResponse(Notification notification) {
                return NotificationResponse.builder()
                                .id(notification.getId())
                                .title(notification.getTitle())
                                .message(notification.getMessage())
                                .type(notification.getType())
                                .isRead(notification.getIsRead())
                                .relatedTransactionId(notification.getRelatedTransactionId())
                                .relatedAccountId(notification.getRelatedAccountId())
                                .createdAt(notification.getCreatedAt())
                                .build();
        }
}
