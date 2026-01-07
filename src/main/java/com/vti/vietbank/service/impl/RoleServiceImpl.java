package com.vti.vietbank.service.impl;

import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.entity.Role;
import com.vti.vietbank.exception.ResourceNotFoundException;
import com.vti.vietbank.repository.RoleRepository;
import com.vti.vietbank.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final RoleRepository roleRepository;

    @Override
    public ApiResponse<Role> getById(Integer id) {
        return ApiResponse.success(roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", "id", id)));
    }

    @Override
    public ApiResponse<Role> getByName(String name) {
        return ApiResponse.success(roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Role", "name", name)));
    }

    @Override
    public Role create(Role role) {
        return null;
    }

    @Override
    public Role update(Role role) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
