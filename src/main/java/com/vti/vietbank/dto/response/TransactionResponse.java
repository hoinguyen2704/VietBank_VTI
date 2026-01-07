package com.vti.vietbank.dto.response;

import com.vti.vietbank.entity.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private String transactionCode;
    private TransactionType transactionType;
    private Long accountId;
    private String accountNumber;
    private String accountName; // Thêm accountName
    private String customerName;
    private Long relatedAccountId;
    private String relatedAccountNumber;
    private String relatedAccountName; // Thêm relatedAccountName
    private String relatedCustomerName;
    private BigDecimal amount;
    private BigDecimal fee;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private String description;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
