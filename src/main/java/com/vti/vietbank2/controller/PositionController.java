package com.vti.vietbank2.controller;

import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.PositionResponse;
import com.vti.vietbank2.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PositionResponse>>> getAll() {
        return ResponseEntity.ok(positionService.getAll());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<PositionResponse>>> getByDepartmentId(@PathVariable Integer departmentId) {
        return ResponseEntity.ok(positionService.getByDepartmentId(departmentId));
    }
}
