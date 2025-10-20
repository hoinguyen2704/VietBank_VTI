package com.vti.vietbank2.dto.response;

import com.vti.vietbank2.entity.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Integer id;
    private String transactionCode;
    private TransactionType transactionType;
    private Integer accountId;
    private String accountNumber;
    private String customerName;
    private Integer relatedAccountId;
    private String relatedAccountNumber;
    private String relatedCustomerName;
    private BigDecimal amount;
    private BigDecimal fee;
    private BigDecimal balanceAfter;
    private String description;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
