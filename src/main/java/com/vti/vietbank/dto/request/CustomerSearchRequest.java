package com.vti.vietbank.dto.request;

import lombok.Data;

@Data
public class CustomerSearchRequest {
    private String fullName;
    private String phoneNumber;
    private String citizenId;
    private String email;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "id";
    private String sortDirection = "asc";
}
