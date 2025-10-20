package com.vti.vietbank2.service;

import com.vti.vietbank2.dto.request.AccountFilterRequest;
import com.vti.vietbank2.dto.request.OpenAccountRequest;
import com.vti.vietbank2.dto.request.UpdateAccountRequest;
import com.vti.vietbank2.dto.response.AccountResponse;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.PageResponse;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    ApiResponse<AccountResponse> openAccount(OpenAccountRequest request);
    ApiResponse<AccountResponse> getByAccountNumber(String accountNumber);
    ApiResponse<PageResponse<AccountResponse>> getAll(AccountFilterRequest filterRequest);
    ApiResponse<AccountResponse> updateAccount(Integer id, UpdateAccountRequest request);
    ApiResponse<Void> closeAccount(Integer id);
    ApiResponse<BigDecimal> getBalanceByAccountNumber(String accountNumber);
    ApiResponse<List<AccountResponse>> getByCustomerId(Integer customerId);
    ApiResponse<BigDecimal> getBalanceById(Integer id);
}


