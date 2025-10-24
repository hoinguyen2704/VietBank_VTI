package com.vti.vietbank2.dto.response;

import com.vti.vietbank2.entity.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Integer id;
    private String accountNumber;
    private String accountName;
    private String customerName;
    private String accountTypeName;
    private BigDecimal balance;
    private AccountStatus status;
    private LocalDateTime openedDate;
    private LocalDateTime closedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
