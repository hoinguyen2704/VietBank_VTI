package com.vti.vietbank.service;

import com.vti.vietbank.dto.request.CreateDepartmentRequest;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    ApiResponse<List<DepartmentResponse>> getAll();
    ApiResponse<DepartmentResponse> create(CreateDepartmentRequest request);
}
