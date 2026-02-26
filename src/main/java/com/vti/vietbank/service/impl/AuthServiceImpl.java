package com.vti.vietbank.service.impl;

import com.vti.vietbank.dto.request.AuthRegistrationRequest;
import com.vti.vietbank.dto.request.CustomerRegistrationRequest;
import com.vti.vietbank.dto.request.LoginRequest;
import com.vti.vietbank.dto.request.RefreshTokenRequest;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.AuthResponse;
import com.vti.vietbank.dto.response.CustomerResponse;
import com.vti.vietbank.entity.User;
import com.vti.vietbank.exception.ResourceNotFoundException;

import com.vti.vietbank.repository.UserRepository;
import com.vti.vietbank.security.CustomUserDetails;
import com.vti.vietbank.security.CustomUserDetailsService;
import com.vti.vietbank.security.JwtTokenProvider;
import com.vti.vietbank.security.TokenBlacklistService;
import com.vti.vietbank.service.AuthService;
import com.vti.vietbank.service.CustomerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    // private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final TokenBlacklistService tokenBlacklistService;
    // private final AuthenticationManager authenticationManager;
    // private final RoleRepository roleRepository;
    // private final IUserService iUserService;
    // private final IRoleService iRoleService;
    // Store refresh tokens (in production, use Redis)
    private final ConcurrentHashMap<String, String> refreshTokenStore = new ConcurrentHashMap<>();

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request) {
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Get user from database
        // User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
        //         .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber", request.getPhoneNumber()));

        // Load user details for Spring Security
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(request.getPhoneNumber());
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            return ApiResponse.error("Invalid credentials");
        }
        // Try plain text comparison as last resort
        // if (!request.getPassword().equals(user.getPassword())) {
        // return ApiResponse.error("Invalid credentials");
        // }

        // Generate tokens
        String accessToken = jwtTokenProvider.generateToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        // Store refresh token
        refreshTokenStore.put(refreshToken, request.getPhoneNumber());

        // Create response
        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getJwtExpiration())
                .userId(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .build();
        System.out.println(jwtTokenProvider.getRoleFromToken(accessToken));
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

            // Load user details (CHỈ 1 LẦN QUERY)
            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);

            // Generate new access token
            String accessToken = jwtTokenProvider.generateToken(userDetails);

            // Lấy role từ authorities
            // String role = userDetails.getAuthorities().stream()
            //         .findFirst()
            //         .map(auth -> auth.getAuthority().replace("ROLE_", ""))
            //         .orElse("CUSTOMER");

            // Create response (dùng userDetails thay vì query User lần nữa)
            AuthResponse authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(request.getRefreshToken())
                    .tokenType("Bearer")
                    .expiresIn(jwtTokenProvider.getJwtExpiration())
                    .userId(userDetails.getId())
                    .username(userDetails.getUsername())
                    .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .build();

            return ApiResponse.success("Token refreshed successfully", authResponse);
        } catch (Exception e) {
            return ApiResponse.error("Failed to refresh token: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<Void> logout(String token) {
        try {
            // Thêm access token vào blacklist
            tokenBlacklistService.blacklistToken(token);

            // Remove refresh token from store (if exists)
            refreshTokenStore.values().removeIf(value -> value.equals(token));
            return ApiResponse.success("Logout successful");
        } catch (Exception e) {
            return ApiResponse.error("Failed to logout: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<CustomUserDetails> getProfile(String token) {
        try {
            // Extract username from token
            String username = jwtTokenProvider.getUsernameFromToken(token);

            // Load user details
            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);

            return ApiResponse.success(userDetails);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get profile: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ApiResponse<CustomerResponse> register(AuthRegistrationRequest request) {

        CustomerRegistrationRequest customer = CustomerRegistrationRequest.builder()
                .phoneNumber(request.getPhoneNumber()).password(request.getPassword()).fullName(request.getFullName())
                .email(request.getEmail()).dateOfBirth(request.getDateOfBirth()).gender(request.getGender())
                .citizenId(request.getCitizenId()).address(request.getAddress()).build();

        CustomerResponse response = customerService.register(customer).getData();
        // LoginRequest loginRequest = new LoginRequest(
        // request.getPhoneNumber(),
        // request.getPassword()
        // );
        // AuthResponse authResponse = login(loginRequest).getData();
        return ApiResponse.success("Customer registered", response);

    }
}
