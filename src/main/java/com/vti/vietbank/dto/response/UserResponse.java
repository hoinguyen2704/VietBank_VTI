package com.vti.vietbank.dto.response;

import com.vti.vietbank.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String phoneNumber;

    private String password;

    private Role role;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}
