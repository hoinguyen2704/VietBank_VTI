package com.vti.vietbank.service;

import com.vti.vietbank.dto.request.StaffRegistrationRequest;
import com.vti.vietbank.dto.request.UpdateStaffRequest;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.PageResponse;
import com.vti.vietbank.dto.response.StaffResponse;

import java.util.List;

public interface StaffService {
    ApiResponse<StaffResponse> register(StaffRegistrationRequest request);
    ApiResponse<PageResponse<StaffResponse>> getAll(Integer page, Integer size, String sortBy, String sortDirection);
    ApiResponse<StaffResponse> getById(Long id);
    ApiResponse<StaffResponse> update(Long id, UpdateStaffRequest request);
    ApiResponse<Void> delete(Long id);
    ApiResponse<List<StaffResponse>> getByDepartmentId(Long departmentId);
}
