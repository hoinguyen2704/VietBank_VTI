package com.vti.vietbank.controller;

import com.vti.vietbank.dto.request.DepositRequest;
import com.vti.vietbank.dto.request.TransactionComplexSearchRequest;
import com.vti.vietbank.dto.request.TransferRequest;
import com.vti.vietbank.dto.request.WithdrawalRequest;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.PageResponse;
import com.vti.vietbank.dto.response.TransactionDetailResponse;
import com.vti.vietbank.dto.response.TransactionResponse;
import com.vti.vietbank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/transactions")
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(@Valid @RequestBody DepositRequest request, Authentication authentication) {
        return ResponseEntity.ok(transactionService.deposit(request, authentication));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(@Valid @RequestBody WithdrawalRequest request, Authentication authentication) {
        return ResponseEntity.ok(transactionService.withdraw(request, authentication));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(@Valid @RequestBody TransferRequest request, Authentication authentication) {
        return ResponseEntity.ok(transactionService.transfer(request, authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getById(id));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getByAccountId(accountId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(transactionService.getByCustomerId(customerId));
    }

    @GetMapping("/account-number/{accountNumber}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getByAccountNumber(@PathVariable String accountNumber, Authentication authentication) {
        return ResponseEntity.ok(transactionService.getByAccountNumber(accountNumber, authentication));
    }

    @GetMapping("/account/{accountNumber}/history")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAccountTransactionHistory(@PathVariable String accountNumber, Authentication authentication) {
        return ResponseEntity.ok(transactionService.getByAccountNumber(accountNumber, authentication));
    }

    @GetMapping("/staff/{staffId}/exists")
    public ResponseEntity<ApiResponse<Boolean>> checkStaffExists(@PathVariable Long staffId) {
        return ResponseEntity.ok(transactionService.checkStaffExists(staffId));
    }
    
    /**
     * Complex search API với nhiều JOIN
     * Tìm kiếm transaction history với đầy đủ thông tin từ nhiều bảng
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN', 'CUSTOMER')")
    public ResponseEntity<ApiResponse<PageResponse<TransactionDetailResponse>>> searchComplexTransactions(
            // @ModelAttribute TransactionComplexSearchRequest searchRequest
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
            searchRequest.setTransactionType(
                    com.vti.vietbank.entity.enums.TransactionType.valueOf(transactionType.toUpperCase()));
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
