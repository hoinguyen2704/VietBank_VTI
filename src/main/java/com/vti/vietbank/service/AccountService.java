package com.vti.vietbank.service;

import com.vti.vietbank.dto.request.AccountFilterRequest;
import com.vti.vietbank.dto.request.OpenAccountRequest;
import com.vti.vietbank.dto.request.UpdateAccountRequest;
import com.vti.vietbank.dto.response.AccountResponse;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.PageResponse;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    ApiResponse<AccountResponse> openAccount(OpenAccountRequest request);
    ApiResponse<AccountResponse> getByAccountNumber(String accountNumber, Authentication authentication);
    ApiResponse<PageResponse<AccountResponse>> getAll(AccountFilterRequest filterRequest);
    ApiResponse<AccountResponse> updateAccount(Long id, UpdateAccountRequest request);
    ApiResponse<Void> closeAccount(Long id);
    ApiResponse<BigDecimal> getBalanceByAccountNumber(String accountNumber);
    ApiResponse<List<AccountResponse>> getByCustomerId(Long customerId);
    ApiResponse<BigDecimal> getBalanceById(Long id);
}


