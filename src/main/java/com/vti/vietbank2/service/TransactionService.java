package com.vti.vietbank2.service;

import com.vti.vietbank2.dto.request.DepositRequest;
import com.vti.vietbank2.dto.request.TransferRequest;
import com.vti.vietbank2.dto.request.WithdrawalRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    ApiResponse<TransactionResponse> deposit(DepositRequest request);
    ApiResponse<TransactionResponse> withdraw(WithdrawalRequest request);
    ApiResponse<TransactionResponse> transfer(TransferRequest request);
    ApiResponse<TransactionResponse> getById(Integer id);
    ApiResponse<List<TransactionResponse>> getByAccountId(Integer accountId);
    ApiResponse<List<TransactionResponse>> getByAccountNumber(String accountNumber);
    ApiResponse<List<TransactionResponse>> getByCustomerId(Integer customerId);
    ApiResponse<Boolean> checkStaffExists(Integer staffId);
}
