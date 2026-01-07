package com.vti.vietbank.service;

import com.vti.vietbank.dto.request.CustomerRegistrationRequest;
import com.vti.vietbank.dto.request.CustomerSearchRequest;
import com.vti.vietbank.dto.request.UpdateCustomerRequest;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.CustomerResponse;
import com.vti.vietbank.dto.response.PageResponse;

import java.util.List;

public interface CustomerService {
    ApiResponse<CustomerResponse> register(CustomerRegistrationRequest request);
    ApiResponse<CustomerResponse> getById(Long id);
    ApiResponse<PageResponse<CustomerResponse>> getAll(CustomerSearchRequest searchRequest);
    ApiResponse<CustomerResponse> update(Long id, UpdateCustomerRequest request);
    ApiResponse<Void> delete(Long id);
    ApiResponse<List<CustomerResponse>> search(CustomerSearchRequest request);
    ApiResponse<Boolean> existsByCitizenId(String citizenId);

}
