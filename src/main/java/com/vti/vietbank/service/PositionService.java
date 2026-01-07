package com.vti.vietbank.service;

import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.PositionResponse;

import java.util.List;

public interface PositionService {
    ApiResponse<List<PositionResponse>> getAll();
    ApiResponse<List<PositionResponse>> getByDepartmentId(Integer departmentId);
}
