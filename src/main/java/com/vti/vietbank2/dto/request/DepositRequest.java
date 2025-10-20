package com.vti.vietbank2.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {
    private Integer accountId;
    private String accountNumber;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @DecimalMin(value = "1000", message = "Minimum deposit amount is 1,000 VND")
    private BigDecimal amount;
    
    private String description;
    
    @NotNull(message = "Created by is required")
    private Integer createdBy; // Staff ID who processes the transaction
}
