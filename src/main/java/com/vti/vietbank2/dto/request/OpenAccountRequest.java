package com.vti.vietbank2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OpenAccountRequest {
    @NotNull(message = "customerId is required")
    private Integer customerId;

    @NotNull(message = "accountTypeId is required")
    private Integer accountTypeId;

    @NotBlank(message = "accountNumber is required")
    @Pattern(regexp = "^[0-9]{10,20}$", message = "accountNumber must be 10-20 digits")
    private String accountNumber;
}


