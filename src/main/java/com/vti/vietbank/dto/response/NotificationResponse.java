package com.vti.vietbank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Integer id;
    private String title;
    private String message;
    private String type;
    private Boolean isRead;
    private Integer relatedTransactionId;
    private Integer relatedAccountId;
    private LocalDateTime createdAt;
}
