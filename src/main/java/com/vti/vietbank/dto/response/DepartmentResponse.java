package com.vti.vietbank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    private Integer id;
    private String name;
    private String description;
    private String managerName;
    private String managerCode;
    private String managerEmail;
    private Integer totalStaff;
    private LocalDateTime createdAt;
}
