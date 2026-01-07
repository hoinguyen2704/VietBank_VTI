package com.vti.vietbank.dto.request;

import com.vti.vietbank.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private String phone;
    private String password;
    private Role role;
}
