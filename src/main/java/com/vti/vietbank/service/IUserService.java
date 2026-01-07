package com.vti.vietbank.service;

import com.vti.vietbank.dto.request.CreateUserRequest;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.CreateUserResponse;
import com.vti.vietbank.dto.response.UserResponse;

public interface IUserService {
    ApiResponse<CreateUserResponse> createUser(CreateUserRequest request);
    ApiResponse<UserResponse> getUserByPhoneNumber(String phoneNumber);
    ApiResponse<Boolean> existsByPhoneNumber(String phoneNumber);
    }
