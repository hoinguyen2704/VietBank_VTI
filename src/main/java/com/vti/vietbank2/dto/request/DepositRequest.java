package com.vti.vietbank2.dto.request;

import com.vti.vietbank2.validation.ValidAccountIdentifier;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ValidAccountIdentifier
public class DepositRequest {
    // Account identifier - chỉ cần một trong hai
    private Integer accountId;           // Optional: Internal ID
    private String accountNumber;        // Optional: User-friendly number
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @DecimalMin(value = "1000", message = "Minimum deposit amount is 1,000 VND")
    private BigDecimal amount;
    
    private String description;
    
    @NotNull(message = "Created by is required")
    private Integer createdBy; // Staff ID who processes the transaction
}
