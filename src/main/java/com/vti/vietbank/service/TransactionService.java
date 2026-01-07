package com.vti.vietbank.service;

import com.vti.vietbank.dto.request.DepositRequest;
import com.vti.vietbank.dto.request.TransferRequest;
import com.vti.vietbank.dto.request.TransactionComplexSearchRequest;
import com.vti.vietbank.dto.request.WithdrawalRequest;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.PageResponse;
import com.vti.vietbank.dto.response.TransactionDetailResponse;
import com.vti.vietbank.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    ApiResponse<TransactionResponse> deposit(DepositRequest request);
    ApiResponse<TransactionResponse> withdraw(WithdrawalRequest request);
    ApiResponse<TransactionResponse> transfer(TransferRequest request);
    ApiResponse<TransactionResponse> getById(Long id);
    ApiResponse<List<TransactionResponse>> getByAccountId(Long accountId);
    ApiResponse<List<TransactionResponse>> getByAccountNumber(String accountNumber);
    ApiResponse<List<TransactionResponse>> getByCustomerId(Long customerId);
    ApiResponse<Boolean> checkStaffExists(Long staffId);
    ApiResponse<PageResponse<TransactionDetailResponse>> searchComplexTransactions(TransactionComplexSearchRequest request);
}
