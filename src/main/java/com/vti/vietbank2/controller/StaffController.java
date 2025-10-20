package com.vti.vietbank2.controller;

import com.vti.vietbank2.dto.request.StaffRegistrationRequest;
import com.vti.vietbank2.dto.request.UpdateStaffRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.PageResponse;
import com.vti.vietbank2.dto.response.StaffResponse;
import com.vti.vietbank2.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<StaffResponse>> register(@Valid @RequestBody StaffRegistrationRequest request) {
        return ResponseEntity.ok(staffService.register(request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StaffResponse>>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        return ResponseEntity.ok(staffService.getAll(page, size, sortBy, sortDirection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(staffService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> update(
            @PathVariable Integer id, 
            @Valid @RequestBody UpdateStaffRequest request) {
        return ResponseEntity.ok(staffService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(staffService.delete(id));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getByDepartmentId(@PathVariable Integer departmentId) {
        return ResponseEntity.ok(staffService.getByDepartmentId(departmentId));
    }
}
