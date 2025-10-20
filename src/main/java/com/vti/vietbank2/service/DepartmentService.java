package com.vti.vietbank2.service;

import com.vti.vietbank2.dto.request.CreateDepartmentRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    ApiResponse<List<DepartmentResponse>> getAll();
    ApiResponse<DepartmentResponse> create(CreateDepartmentRequest request);
}
