package com.vti.vietbank2.service;

import com.vti.vietbank2.dto.request.CustomerRegistrationRequest;
import com.vti.vietbank2.dto.request.CustomerSearchRequest;
import com.vti.vietbank2.dto.request.UpdateCustomerRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.CustomerResponse;
import com.vti.vietbank2.dto.response.PageResponse;

import java.util.List;

public interface CustomerService {
    ApiResponse<CustomerResponse> register(CustomerRegistrationRequest request);
    ApiResponse<CustomerResponse> getById(Integer id);
    ApiResponse<PageResponse<CustomerResponse>> getAll(CustomerSearchRequest searchRequest);
    ApiResponse<CustomerResponse> update(Integer id, UpdateCustomerRequest request);
    ApiResponse<Void> delete(Integer id);
    ApiResponse<List<CustomerResponse>> search(CustomerSearchRequest searchRequest);
}


