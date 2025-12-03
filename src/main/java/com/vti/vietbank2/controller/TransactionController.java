package com.vti.vietbank2.controller;

import com.vti.vietbank2.dto.request.DepositRequest;
import com.vti.vietbank2.dto.request.TransactionComplexSearchRequest;
import com.vti.vietbank2.dto.request.TransferRequest;
import com.vti.vietbank2.dto.request.WithdrawalRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.PageResponse;
import com.vti.vietbank2.dto.response.TransactionDetailResponse;
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
    
    /**
     * Complex search API với nhiều JOIN
     * Tìm kiếm transaction history với đầy đủ thông tin từ nhiều bảng
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<TransactionDetailResponse>>> searchComplexTransactions(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String customerPhone,
            @RequestParam(required = false) String citizenId,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) Integer accountId,
            @RequestParam(required = false) String relatedAccountNumber,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) String transactionCode,
            @RequestParam(required = false) java.math.BigDecimal minAmount,
            @RequestParam(required = false) java.math.BigDecimal maxAmount,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Integer createdByStaffId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        TransactionComplexSearchRequest searchRequest = new TransactionComplexSearchRequest();
        searchRequest.setCustomerName(customerName);
        searchRequest.setCustomerPhone(customerPhone);
        searchRequest.setCitizenId(citizenId);
        searchRequest.setAccountNumber(accountNumber);
        searchRequest.setAccountId(accountId);
        searchRequest.setRelatedAccountNumber(relatedAccountNumber);
        if (transactionType != null) {
            searchRequest.setTransactionType(com.vti.vietbank2.entity.enums.TransactionType.valueOf(transactionType.toUpperCase()));
        }
        searchRequest.setTransactionCode(transactionCode);
        searchRequest.setMinAmount(minAmount);
        searchRequest.setMaxAmount(maxAmount);
        if (fromDate != null) {
            searchRequest.setFromDate(java.time.LocalDateTime.parse(fromDate));
        }
        if (toDate != null) {
            searchRequest.setToDate(java.time.LocalDateTime.parse(toDate));
        }
        searchRequest.setCreatedByStaffId(createdByStaffId);
        searchRequest.setPage(page);
        searchRequest.setSize(size);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);
        
        return ResponseEntity.ok(transactionService.searchComplexTransactions(searchRequest));
    }
}
