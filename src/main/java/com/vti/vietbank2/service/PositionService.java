package com.vti.vietbank2.service;

import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.PositionResponse;

import java.util.List;

public interface PositionService {
    ApiResponse<List<PositionResponse>> getAll();
    ApiResponse<List<PositionResponse>> getByDepartmentId(Integer departmentId);
}
