package com.vti.vietbank2.service;

import com.vti.vietbank2.dto.request.LoginRequest;
import com.vti.vietbank2.dto.request.RefreshTokenRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.AuthResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    ApiResponse<AuthResponse> login(LoginRequest request);
    ApiResponse<AuthResponse> refreshToken(RefreshTokenRequest request);
    ApiResponse<Void> logout(String token);
    ApiResponse<UserDetails> getProfile(String token);
}
