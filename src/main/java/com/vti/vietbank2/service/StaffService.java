package com.vti.vietbank2.service;

import com.vti.vietbank2.dto.request.StaffRegistrationRequest;
import com.vti.vietbank2.dto.request.UpdateStaffRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.PageResponse;
import com.vti.vietbank2.dto.response.StaffResponse;

import java.util.List;

public interface StaffService {
    ApiResponse<StaffResponse> register(StaffRegistrationRequest request);
    ApiResponse<PageResponse<StaffResponse>> getAll(Integer page, Integer size, String sortBy, String sortDirection);
    ApiResponse<StaffResponse> getById(Integer id);
    ApiResponse<StaffResponse> update(Integer id, UpdateStaffRequest request);
    ApiResponse<Void> delete(Integer id);
    ApiResponse<List<StaffResponse>> getByDepartmentId(Integer departmentId);
}
