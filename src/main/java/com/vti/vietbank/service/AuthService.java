package com.vti.vietbank.service;

import com.vti.vietbank.dto.request.AuthRegistrationRequest;
import com.vti.vietbank.dto.request.LoginRequest;
import com.vti.vietbank.dto.request.RefreshTokenRequest;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.AuthResponse;
import com.vti.vietbank.dto.response.CustomerResponse;
import com.vti.vietbank.security.CustomUserDetails;

public interface AuthService {
    ApiResponse<AuthResponse> login(LoginRequest request);

    ApiResponse<AuthResponse> refreshToken(RefreshTokenRequest request);

    ApiResponse<Void> logout(String token);

    ApiResponse<CustomUserDetails> getProfile(String token);

    ApiResponse<CustomerResponse> register(AuthRegistrationRequest request);
}
