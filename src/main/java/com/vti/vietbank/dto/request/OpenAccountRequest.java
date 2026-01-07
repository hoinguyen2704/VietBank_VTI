package com.vti.vietbank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OpenAccountRequest {
    @NotBlank(message = "phoneNumber is required")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number must be 10-11 digits")
    private String phoneNumber;

    @NotNull(message = "accountTypeId is required")
    private Integer accountTypeId;

    @NotBlank(message = "accountNumber is required")
    @Pattern(regexp = "^[0-9]{10,20}$", message = "accountNumber must be 10-20 digits")
    private String accountNumber;

    private String accountName; // Optional - sẽ tự động tạo từ tên khách hàng nếu không có
}


