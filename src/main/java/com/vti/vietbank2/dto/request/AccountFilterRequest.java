package com.vti.vietbank2.dto.request;

import com.vti.vietbank2.entity.enums.AccountStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountFilterRequest {
    private String accountNumber;
    private String accountName;
    private Integer customerId;
    private Integer accountTypeId;
    private AccountStatus status;
    private BigDecimal minBalance;
    private BigDecimal maxBalance;
    private LocalDateTime openedFrom;
    private LocalDateTime openedTo;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "id";
    private String sortDirection = "asc";
}
