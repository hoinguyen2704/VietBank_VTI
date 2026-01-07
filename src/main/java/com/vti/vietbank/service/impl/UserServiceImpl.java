package com.vti.vietbank.service.impl;

import com.vti.vietbank.dto.request.CreateUserRequest;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.CreateUserResponse;
import com.vti.vietbank.dto.response.UserResponse;
import com.vti.vietbank.entity.User;
import com.vti.vietbank.repository.UserRepository;
import com.vti.vietbank.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;

    @Override
    public ApiResponse<CreateUserResponse> createUser(CreateUserRequest request) {
        User user = new User();
        user.setPhoneNumber(request.getPhone());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        userRepository.save(user);

        CreateUserResponse response = new CreateUserResponse();
        response.setPhone(user.getPhoneNumber());
        response.setRole(request.getRole());
        response.setCreateAt(user.getCreateAt());

        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse<UserResponse> getUserByPhoneNumber(String PhoneNumber) {
            Optional<User> user = userRepository.findByPhoneNumber(PhoneNumber);
            UserResponse response = new UserResponse();
            response.setPhoneNumber(user.get().getPhoneNumber());
            response.setRole(user.get().getRole());
            response.setCreateAt(user.get().getCreateAt());
            return ApiResponse.success(response);
    }

    @Override
    public ApiResponse<Boolean> existsByPhoneNumber(String phoneNumber) {
        return ApiResponse.success(userRepository.existsByPhoneNumber(phoneNumber));
    }

}
