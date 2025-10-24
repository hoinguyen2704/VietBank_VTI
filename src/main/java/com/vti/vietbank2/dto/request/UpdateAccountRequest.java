package com.vti.vietbank2.dto.request;

import com.vti.vietbank2.entity.enums.AccountStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateAccountRequest {
    @NotNull(message = "Account status is required")
    private AccountStatus status;
    
    private String accountName;
    
    private LocalDateTime closedDate;
}
