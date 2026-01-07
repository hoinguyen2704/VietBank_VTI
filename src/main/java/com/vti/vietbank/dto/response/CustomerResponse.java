package com.vti.vietbank.dto.response;

import com.vti.vietbank.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private Long id;
    private String phoneNumber;
    private String fullName;
    private String email;
    private Date dateOfBirth;
    private Gender gender;
    private String citizenId;
    private String address;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


