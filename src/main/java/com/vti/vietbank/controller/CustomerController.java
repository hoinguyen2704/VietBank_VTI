package com.vti.vietbank.controller;

import com.vti.vietbank.dto.request.CustomerRegistrationRequest;
import com.vti.vietbank.dto.request.CustomerSearchRequest;
import com.vti.vietbank.dto.request.UpdateCustomerRequest;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.CustomerResponse;
import com.vti.vietbank.dto.response.PageResponse;
import com.vti.vietbank.service.AccountService;
import com.vti.vietbank.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final AccountService accountService;
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CustomerResponse>>> getAll(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String citizenId,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        CustomerSearchRequest searchRequest = new CustomerSearchRequest();
        searchRequest.setFullName(fullName);
        searchRequest.setPhoneNumber(phoneNumber);
        searchRequest.setCitizenId(citizenId);
        searchRequest.setEmail(email);
        searchRequest.setPage(page);
        searchRequest.setSize(size);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);
        
        return ResponseEntity.ok(customerService.getAll(searchRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<CustomerResponse>> register(@Valid @RequestBody CustomerRegistrationRequest request) {
        return ResponseEntity.ok(customerService.register(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request) {
        return ResponseEntity.ok(customerService.update(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.delete(id));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> search(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String citizenId,
            @RequestParam(required = false) String email) {
        
        CustomerSearchRequest searchRequest = new CustomerSearchRequest();
        searchRequest.setFullName(fullName);
        searchRequest.setPhoneNumber(phoneNumber);
        searchRequest.setCitizenId(citizenId);
        searchRequest.setEmail(email);
        System.out.println(searchRequest);
        return ResponseEntity.ok(customerService.search(searchRequest));
    }
    
    @GetMapping("/{id}/accounts")
    public ResponseEntity<ApiResponse<List<com.vti.vietbank.dto.response.AccountResponse>>> getAccounts(@PathVariable Long id) {
        // Use existing AccountService method
        return ResponseEntity.ok(accountService.getByCustomerId(id));
    }
}


