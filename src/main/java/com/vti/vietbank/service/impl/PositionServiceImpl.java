package com.vti.vietbank.service.impl;

import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.PositionResponse;
import com.vti.vietbank.entity.Position;
import com.vti.vietbank.repository.PositionRepository;
import com.vti.vietbank.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    @Override
    public ApiResponse<List<PositionResponse>> getAll() {
        List<Position> positions = positionRepository.findAll();
        List<PositionResponse> responses = positions.stream()
                .map(this::convertToPositionResponse)
                .toList();
        
        return ApiResponse.success(responses);
    }

    @Override
    public ApiResponse<List<PositionResponse>> getByDepartmentId(Integer departmentId) {
        List<Position> positions = positionRepository.findByDepartment_Id(departmentId);
        List<PositionResponse> responses = positions.stream()
                .map(this::convertToPositionResponse)
                .toList();
        
        return ApiResponse.success(responses);
    }

    private PositionResponse convertToPositionResponse(Position position) {
        return new PositionResponse(
                position.getId(),
                position.getName(),
                position.getDescription(),
                position.getLevel(),
                position.getDepartment().getName(),
                position.getDepartment().getId(),
                position.getCreateAt()
        );
    }
}
