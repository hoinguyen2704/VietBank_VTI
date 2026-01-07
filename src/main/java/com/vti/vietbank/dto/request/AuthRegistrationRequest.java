package com.vti.vietbank.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vti.vietbank.entity.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRegistrationRequest {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number must be 10-11 digits")
    private String phoneNumber;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;
    
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirth;
    
    private Gender gender;
    
    @Pattern(regexp = "^[0-9]{9,12}$", message = "Citizen ID must be 9-12 digits")
    private String citizenId;
    
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;
}
