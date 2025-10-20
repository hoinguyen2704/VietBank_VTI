package com.vti.vietbank2.service.impl;

import com.vti.vietbank2.dto.request.CreateDepartmentRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.DepartmentResponse;
import com.vti.vietbank2.entity.Department;
import com.vti.vietbank2.exception.DuplicateResourceException;
import com.vti.vietbank2.repository.DepartmentRepository;
import com.vti.vietbank2.repository.StaffRepository;
import com.vti.vietbank2.service.DepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final StaffRepository staffRepository;

    @Override
    public ApiResponse<List<DepartmentResponse>> getAll() {
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentResponse> responses = departments.stream()
                .map(this::convertToDepartmentResponse)
                .toList();
        
        return ApiResponse.success(responses);
    }

    @Override
    @Transactional
    public ApiResponse<DepartmentResponse> create(CreateDepartmentRequest request) {
        // Check if department name already exists
        if (departmentRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Department", "name", request.getName());
        }

        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department = departmentRepository.save(department);

        DepartmentResponse response = convertToDepartmentResponse(department);
        return ApiResponse.success("Department created successfully", response);
    }

    private DepartmentResponse convertToDepartmentResponse(Department department) {
        long totalStaff = staffRepository.countByDepartment_IdAndIsActiveTrue(department.getId());
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getDescription(),
                null, // Manager name - will be populated from view if needed
                null, // Manager code - will be populated from view if needed
                null, // Manager email - will be populated from view if needed
                (int) totalStaff,
                department.getCreateAt()
        );
    }
}
