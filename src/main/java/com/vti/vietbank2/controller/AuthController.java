package com.vti.vietbank2.controller;

import com.vti.vietbank2.dto.request.LoginRequest;
import com.vti.vietbank2.dto.request.RefreshTokenRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.AuthResponse;
import com.vti.vietbank2.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix if exists
        String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        return ResponseEntity.ok(authService.logout(actualToken));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDetails>> getProfile(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix if exists
        String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        return ResponseEntity.ok(authService.getProfile(actualToken));
    }
}
