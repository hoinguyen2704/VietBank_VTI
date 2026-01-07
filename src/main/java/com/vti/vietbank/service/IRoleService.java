package com.vti.vietbank.service;

import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.entity.Role;

public interface IRoleService {
    ApiResponse<Role> getById(Integer id);

    ApiResponse<Role> getByName(String name);

    Role create(Role role);

    Role update(Role role);

    void delete(Integer id);
}
