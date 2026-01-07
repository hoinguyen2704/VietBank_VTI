package com.vti.vietbank.dto.response;

import com.vti.vietbank.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateUserResponse {
    private String phone;
    private Role role;
    private LocalDateTime createAt;

}
