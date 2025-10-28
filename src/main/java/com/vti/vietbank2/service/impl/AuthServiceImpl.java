package com.vti.vietbank2.service.impl;

import com.vti.vietbank2.dto.request.LoginRequest;
import com.vti.vietbank2.dto.request.RefreshTokenRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.AuthResponse;
import com.vti.vietbank2.entity.User;
import com.vti.vietbank2.exception.ResourceNotFoundException;
import com.vti.vietbank2.repository.UserRepository;
import com.vti.vietbank2.security.CustomUserDetailsService;
import com.vti.vietbank2.security.JwtTokenProvider;
import com.vti.vietbank2.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    // Store refresh tokens (in production, use Redis)
    private final ConcurrentHashMap<String, String> refreshTokenStore = new ConcurrentHashMap<>();

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request) {
        // Get user from database
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber", request.getPhoneNumber()));

        // Hash input password with MD5
        String hashedPassword = hashMD5(request.getPassword());
        
        // Check password - support MD5 hash
        if (!hashedPassword.equals(user.getPassword())) {
            // Try BCrypt matching as fallback
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getPhoneNumber());
                if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                    return ApiResponse.error("Invalid credentials");
                }
            } catch (Exception e) {
                // Try plain text comparison as last resort
                if (!request.getPassword().equals(user.getPassword())) {
                    return ApiResponse.error("Invalid credentials");
                }
            }
        }
        
        // Load user details for Spring Security
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getPhoneNumber());

        // Generate tokens
        String accessToken = jwtTokenProvider.generateToken(userDetails, user.getId(), user.getRole().getName());
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        // Store refresh token
        refreshTokenStore.put(refreshToken, request.getPhoneNumber());

        // Create response
        AuthResponse authResponse = new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwtTokenProvider.getJwtExpiration(), // Assuming you add this getter
                user.getId(),
                user.getPhoneNumber(),
                user.getRole().getName()
        );

        return ApiResponse.success("Login successful", authResponse);
    }

    @Override
    public ApiResponse<AuthResponse> refreshToken(RefreshTokenRequest request) {
        try {
            // Validate refresh token
            if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
                return ApiResponse.error("Invalid refresh token");
            }

            // Check if refresh token exists
            String phoneNumber = refreshTokenStore.get(request.getRefreshToken());
            if (phoneNumber == null) {
                return ApiResponse.error("Refresh token not found");
            }

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);
            User user = userRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber", phoneNumber));

            // Generate new access token
            String accessToken = jwtTokenProvider.generateToken(userDetails, user.getId(), user.getRole().getName());

            // Create response
            AuthResponse authResponse = new AuthResponse(
                    accessToken,
                    request.getRefreshToken(),
                    "Bearer",
                    jwtTokenProvider.getJwtExpiration(),
                    user.getId(),
                    user.getPhoneNumber(),
                    user.getRole().getName()
            );

            return ApiResponse.success("Token refreshed successfully", authResponse);
        } catch (Exception e) {
            return ApiResponse.error("Failed to refresh token: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<Void> logout(String token) {
        try {
            // Remove refresh token from store (if exists)
            refreshTokenStore.values().removeIf(value -> value.equals(token));
            return ApiResponse.success("Logout successful");
        } catch (Exception e) {
            return ApiResponse.error("Failed to logout: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<UserDetails> getProfile(String token) {
        try {
            // Extract username from token
            String username = jwtTokenProvider.getUsernameFromToken(token);
            
            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            return ApiResponse.success(userDetails);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get profile: " + e.getMessage());
        }
    }

    /**
     * Hash password using MD5 algorithm
     */
    private String hashMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
