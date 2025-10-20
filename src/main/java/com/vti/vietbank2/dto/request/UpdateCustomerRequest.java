package com.vti.vietbank2.dto.request;

import com.vti.vietbank2.entity.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateCustomerRequest {
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @Email(message = "Email should be valid")
    private String email;
    
    private Date dateOfBirth;
    
    private Gender gender;
    
    @Pattern(regexp = "^[0-9]{9,12}$", message = "Citizen ID must be 9-12 digits")
    private String citizenId;
    
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    private boolean isDeleted;
}
