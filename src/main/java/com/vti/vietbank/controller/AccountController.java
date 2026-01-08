package com.vti.vietbank.controller;

import com.vti.vietbank.dto.request.AccountFilterRequest;
import com.vti.vietbank.dto.request.OpenAccountRequest;
import com.vti.vietbank.dto.request.UpdateAccountRequest;
import com.vti.vietbank.dto.response.AccountResponse;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.PageResponse;
import com.vti.vietbank.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AccountResponse>>> getAllAccounts(
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String accountName,
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) Integer accountTypeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) BigDecimal minBalance,
            @RequestParam(required = false) BigDecimal maxBalance,
            @RequestParam(required = false) String openedFrom,
            @RequestParam(required = false) String openedTo,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        AccountFilterRequest filterRequest = new AccountFilterRequest();
        filterRequest.setAccountNumber(accountNumber);
        filterRequest.setAccountName(accountName);
        filterRequest.setCustomerId(customerId);
        filterRequest.setAccountTypeId(accountTypeId);
        if (status != null) {
            filterRequest.setStatus(com.vti.vietbank.entity.enums.AccountStatus.valueOf(status.toUpperCase()));
        }
        filterRequest.setMinBalance(minBalance);
        filterRequest.setMaxBalance(maxBalance);
        if (openedFrom != null) {
            filterRequest.setOpenedFrom(java.time.LocalDateTime.parse(openedFrom));
        }
        if (openedTo != null) {
            filterRequest.setOpenedTo(java.time.LocalDateTime.parse(openedTo));
        }
        filterRequest.setPage(page);
        filterRequest.setSize(size);
        filterRequest.setSortBy(sortBy);
        filterRequest.setSortDirection(sortDirection);
        
        return ResponseEntity.ok(accountService.getAll(filterRequest));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> openAccount(@Valid @RequestBody OpenAccountRequest request) {
        return ResponseEntity.ok(accountService.openAccount(request));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> getByAccountNumber(@PathVariable String accountNumber, Authentication authentication) {
        return ResponseEntity.ok(accountService.getByAccountNumber(accountNumber, authentication));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAccountRequest request) {
        return ResponseEntity.ok(accountService.updateAccount(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> closeAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.closeAccount(id));
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(accountService.getByCustomerId(customerId));
    }
    
    @GetMapping("/{id}/balance")
    public ResponseEntity<ApiResponse<BigDecimal>> getBalanceById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getBalanceById(id));
    }
    
    @GetMapping("account-number/{accountNumber}/balance")
    public ResponseEntity<ApiResponse<BigDecimal>> getBalanceByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getBalanceByAccountNumber(accountNumber));
    }
}


