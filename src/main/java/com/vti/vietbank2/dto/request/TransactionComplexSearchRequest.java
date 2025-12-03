package com.vti.vietbank2.dto.request;

import com.vti.vietbank2.entity.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionComplexSearchRequest {
    // Search by customer
    private String customerName;
    private String customerPhone;
    private String citizenId;
    
    // Search by account
    private String accountNumber;
    private Integer accountId;
    private String relatedAccountNumber;
    
    // Search by transaction
    private TransactionType transactionType;
    private String transactionCode;
    
    // Search by amount range
    private java.math.BigDecimal minAmount;
    private java.math.BigDecimal maxAmount;
    
    // Search by date range
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    
    // Search by staff
    private Integer createdByStaffId;
    private String createdByStaffName;
    
    // Pagination
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "created_at";
    private String sortDirection = "desc";
}

