package com.vti.vietbank2.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateStaffRequest {
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @Email(message = "Email should be valid")
    private String email;
    
    @Size(max = 20, message = "Employee code must not exceed 20 characters")
    private String employeeCode;
    
    private Integer departmentId;
    private Integer positionId;
    private Boolean isActive;
}
