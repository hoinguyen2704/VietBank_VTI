package com.vti.vietbank.service;

import com.vti.vietbank.dto.request.DepositRequest;
import com.vti.vietbank.dto.request.TransferRequest;
import com.vti.vietbank.dto.request.TransactionComplexSearchRequest;
import com.vti.vietbank.dto.request.WithdrawalRequest;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.PageResponse;
import com.vti.vietbank.dto.response.TransactionDetailResponse;
import com.vti.vietbank.dto.response.TransactionResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TransactionService {
    ApiResponse<TransactionResponse> deposit(DepositRequest request, Authentication authentication);
    ApiResponse<TransactionResponse> withdraw(WithdrawalRequest request, Authentication authentication);
    ApiResponse<TransactionResponse> transfer(TransferRequest request, Authentication authentication);
    ApiResponse<TransactionResponse> getById(Long id);
    ApiResponse<List<TransactionResponse>> getByAccountId(Long accountId);
    ApiResponse<List<TransactionResponse>> getByAccountNumber(String accountNumber, Authentication authentication);
    ApiResponse<List<TransactionResponse>> getByCustomerId(Long customerId);
    ApiResponse<Boolean> checkStaffExists(Long staffId);
    ApiResponse<PageResponse<TransactionDetailResponse>> searchComplexTransactions(TransactionComplexSearchRequest request);
}
