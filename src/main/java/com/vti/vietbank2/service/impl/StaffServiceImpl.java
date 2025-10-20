package com.vti.vietbank2.service.impl;

import com.vti.vietbank2.dto.request.StaffRegistrationRequest;
import com.vti.vietbank2.dto.request.UpdateStaffRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.PageResponse;
import com.vti.vietbank2.dto.response.StaffResponse;
import com.vti.vietbank2.entity.*;
import com.vti.vietbank2.exception.DuplicateResourceException;
import com.vti.vietbank2.exception.ResourceNotFoundException;
import com.vti.vietbank2.repository.*;
import com.vti.vietbank2.service.StaffService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    @Override
    @Transactional
    public ApiResponse<StaffResponse> register(StaffRegistrationRequest request) {
        // Check if phone number already exists
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateResourceException("User", "phoneNumber", request.getPhoneNumber());
        }
        
        // Check if employee code already exists
        if (staffRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new DuplicateResourceException("Staff", "employeeCode", request.getEmployeeCode());
        }

        // Get STAFF role
        Role role = roleRepository.findByName("STAFF")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "STAFF"));

        // Validate department exists
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        // Validate position exists
        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new ResourceNotFoundException("Position", "id", request.getPositionId()));

        // Create user
        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(request.getPassword()); // TODO: encode later when adding security
        user.setRole(role);
        user = userRepository.save(user);

        // Create staff
        Staff staff = new Staff();
        staff.setUser(user);
        staff.setFullName(request.getFullName());
        staff.setEmail(request.getEmail());
        staff.setEmployeeCode(request.getEmployeeCode());
        staff.setDepartment(department);
        staff.setPosition(position);
        staff.setActive(true);
        staff = staffRepository.save(staff);

        StaffResponse response = convertToStaffResponse(staff);
        return ApiResponse.success("Staff registered successfully", response);
    }

    @Override
    public ApiResponse<PageResponse<StaffResponse>> getAll(Integer page, Integer size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(
            sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, 
            sortBy
        );
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Staff> staffPage = staffRepository.findAll(pageable);
        
        List<StaffResponse> staffResponses = staffPage.getContent().stream()
            .map(this::convertToStaffResponse)
            .toList();
        
        PageResponse<StaffResponse> pageResponse = new PageResponse<>(
            staffResponses,
            staffPage.getNumber(),
            staffPage.getSize(),
            staffPage.getTotalElements(),
            staffPage.getTotalPages(),
            staffPage.isFirst(),
            staffPage.isLast()
        );
        
        return ApiResponse.success(pageResponse);
    }

    @Override
    public ApiResponse<StaffResponse> getById(Integer id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));
        
        StaffResponse response = convertToStaffResponse(staff);
        return ApiResponse.success(response);
    }

    @Override
    @Transactional
    public ApiResponse<StaffResponse> update(Integer id, UpdateStaffRequest request) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));
        
        // Check for duplicate employee code if provided
        if (request.getEmployeeCode() != null && !request.getEmployeeCode().equals(staff.getEmployeeCode())) {
            if (staffRepository.existsByEmployeeCode(request.getEmployeeCode())) {
                throw new DuplicateResourceException("Staff", "employeeCode", request.getEmployeeCode());
            }
        }
        
        // Update staff information
        if (request.getFullName() != null) {
            staff.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            staff.setEmail(request.getEmail());
        }
        if (request.getEmployeeCode() != null) {
            staff.setEmployeeCode(request.getEmployeeCode());
        }
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
            staff.setDepartment(department);
        }
        if (request.getPositionId() != null) {
            Position position = positionRepository.findById(request.getPositionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Position", "id", request.getPositionId()));
            staff.setPosition(position);
        }
        if (request.getIsActive() != null) {
            staff.setActive(request.getIsActive());
        }
        
        staff = staffRepository.save(staff);
        
        StaffResponse response = convertToStaffResponse(staff);
        return ApiResponse.success("Staff updated successfully", response);
    }

    @Override
    @Transactional
    public ApiResponse<Void> delete(Integer id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));
        
        if (!staff.isActive()) {
            return new ApiResponse<>(true, "Staff is already deactivated", null, LocalDateTime.now());
        }
        
        staff.setActive(false);
        staffRepository.save(staff);
        
        return new ApiResponse<>(true, "Staff deactivated successfully", null, LocalDateTime.now());
    }

    @Override
    public ApiResponse<List<StaffResponse>> getByDepartmentId(Integer departmentId) {
        List<Staff> staffList = staffRepository.findByDepartment_Id(departmentId);
        List<StaffResponse> responses = staffList.stream()
                .map(this::convertToStaffResponse)
                .toList();
        
        return ApiResponse.success(responses);
    }

    private StaffResponse convertToStaffResponse(Staff staff) {
        return new StaffResponse(
                staff.getId(),
                staff.getUser().getPhoneNumber(),
                staff.getFullName(),
                staff.getEmail(),
                staff.getEmployeeCode(),
                staff.getDepartment().getName(),
                staff.getPosition().getName(),
                staff.getPosition().getLevel(),
                staff.isActive(),
                staff.getCreateAt(),
                staff.getUpdateAt()
        );
    }
}
