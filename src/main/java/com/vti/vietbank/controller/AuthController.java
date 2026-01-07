package com.vti.vietbank.controller;

import com.vti.vietbank.dto.request.AuthRegistrationRequest;
import com.vti.vietbank.dto.request.LoginRequest;
import com.vti.vietbank.dto.request.RefreshTokenRequest;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.AuthResponse;
import com.vti.vietbank.dto.response.CustomerResponse;
import com.vti.vietbank.security.CustomUserDetails;
import com.vti.vietbank.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API xác thực và quản lý JWT token")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Đăng ký", description = "Đăng ký tài khoản mới")
    public ResponseEntity<ApiResponse<CustomerResponse>> register(@Valid @RequestBody AuthRegistrationRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }


    @PostMapping("/login")
    @Operation(summary = "Đăng nhập", description = "Đăng nhập bằng số điện thoại và mật khẩu để nhận JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Làm mới token", description = "Sử dụng refresh token để lấy access token mới")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "Đăng xuất", description = "Đăng xuất và vô hiệu hóa token")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix if exists
        String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        return ResponseEntity.ok(authService.logout(actualToken));
    }

    @GetMapping("/profile")
    @Operation(summary = "Lấy thông tin profile", description = "Lấy thông tin user hiện tại từ JWT token", hidden = true)
    public ResponseEntity<ApiResponse<CustomUserDetails>> getProfile(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix if exists
        String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        return ResponseEntity.ok(authService.getProfile(actualToken));
    }
}
