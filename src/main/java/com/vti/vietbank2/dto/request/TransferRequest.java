package com.vti.vietbank2.dto.request;

import com.vti.vietbank2.validation.ValidTransferAccountIdentifiers;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ValidTransferAccountIdentifiers
public class TransferRequest {
    // From account - chỉ cần một trong hai
    private Integer fromAccountId;
    private String fromAccountNumber;
    
    // To account - chỉ cần một trong hai
    private Integer toAccountId;
    private String toAccountNumber;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @DecimalMin(value = "1000", message = "Minimum transfer amount is 1,000 VND")
    private BigDecimal amount;
    
    private String description;
    
    @NotNull(message = "Created by is required")
    private Integer createdBy; // Staff ID who processes the transaction
}
