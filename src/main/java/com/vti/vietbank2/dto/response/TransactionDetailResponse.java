package com.vti.vietbank2.dto.response;

import com.vti.vietbank2.entity.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO response cho transaction detail với đầy đủ thông tin từ nhiều bảng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailResponse {
    // Transaction info
    private Integer transactionId;
    private String transactionCode;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private String description;
    private LocalDateTime createdAt;
    
    // Main Account info
    private Integer accountId;
    private String accountNumber;
    private String accountName;
    private BigDecimal accountBalance;
    
    // Main Customer info
    private Integer customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerCitizenId;
    
    // Related Account info (for transfers)
    private Integer relatedAccountId;
    private String relatedAccountNumber;
    private String relatedAccountName;
    
    // Related Customer info
    private Integer relatedCustomerId;
    private String relatedCustomerName;
    private String relatedCustomerPhone;
    
    // Staff who created transaction
    private Integer createdByStaffId;
    private String createdByStaffName;
    private String createdByStaffCode;
    private String createdByStaffEmail;
}

