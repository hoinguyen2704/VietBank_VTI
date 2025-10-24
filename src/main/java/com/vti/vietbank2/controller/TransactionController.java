package com.vti.vietbank2.controller;

import com.vti.vietbank2.dto.request.DepositRequest;
import com.vti.vietbank2.dto.request.TransferRequest;
import com.vti.vietbank2.dto.request.WithdrawalRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.TransactionResponse;
import com.vti.vietbank2.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(@Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(transactionService.deposit(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(@Valid @RequestBody WithdrawalRequest request) {
        return ResponseEntity.ok(transactionService.withdraw(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transactionService.transfer(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(transactionService.getById(id));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getByAccountId(@PathVariable Integer accountId) {
        return ResponseEntity.ok(transactionService.getByAccountId(accountId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getByCustomerId(@PathVariable Integer customerId) {
        return ResponseEntity.ok(transactionService.getByCustomerId(customerId));
    }

    @GetMapping("/account-number/{accountNumber}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getByAccountNumber(accountNumber));
    }

    @GetMapping("/account/{accountNumber}/history")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAccountTransactionHistory(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getByAccountNumber(accountNumber));
    }

    @GetMapping("/staff/{staffId}/exists")
    public ResponseEntity<ApiResponse<Boolean>> checkStaffExists(@PathVariable Integer staffId) {
        return ResponseEntity.ok(transactionService.checkStaffExists(staffId));
    }
}
