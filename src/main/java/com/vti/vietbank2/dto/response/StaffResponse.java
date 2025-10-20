package com.vti.vietbank2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponse {
    private Integer id;
    private String phoneNumber;
    private String fullName;
    private String email;
    private String employeeCode;
    private String departmentName;
    private String positionName;
    private Integer positionLevel;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
