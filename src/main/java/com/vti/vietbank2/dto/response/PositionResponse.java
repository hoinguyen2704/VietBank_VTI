package com.vti.vietbank2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionResponse {
    private Integer id;
    private String name;
    private String description;
    private Integer level;
    private String departmentName;
    private Integer departmentId;
    private LocalDateTime createdAt;
}
