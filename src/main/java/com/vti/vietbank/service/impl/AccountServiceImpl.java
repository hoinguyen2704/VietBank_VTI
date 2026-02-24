package com.vti.vietbank.service.impl;

import com.vti.vietbank.dto.request.AccountFilterRequest;
import com.vti.vietbank.dto.request.OpenAccountRequest;
import com.vti.vietbank.dto.request.UpdateAccountRequest;
import com.vti.vietbank.dto.response.AccountResponse;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.PageResponse;
import com.vti.vietbank.entity.Account;
import com.vti.vietbank.entity.AccountType;
import com.vti.vietbank.entity.Account_;
import com.vti.vietbank.entity.Customer;
import com.vti.vietbank.entity.enums.AccountStatus;
import com.vti.vietbank.exception.DuplicateResourceException;
import com.vti.vietbank.exception.ResourceNotFoundException;
import com.vti.vietbank.repository.AccountRepository;
import com.vti.vietbank.repository.AccountTypeRepository;
import com.vti.vietbank.repository.CustomerRepository;
import com.vti.vietbank.security.CustomUserDetails;
import com.vti.vietbank.service.AccountService;
import com.vti.vietbank.service.CustomerService;
import com.vti.vietbank.specification.AccountSpecs;
import com.vti.vietbank.util.AccountNameGenerator;
import com.vti.vietbank.util.AccountResolver;
import jakarta.transaction.Transactional;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final CustomerService customerService;
    private final AccountResolver accountResolver;

    @Override
    @Transactional
    public ApiResponse<AccountResponse> openAccount(OpenAccountRequest request) {
        if (accountRepository.existsByAccountNumber(request.getAccountNumber())) {
            throw new DuplicateResourceException("Account", "accountNumber", request.getAccountNumber());
        }

        Customer customer = customerRepository.findByUser_PhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "phoneNumber", request.getPhoneNumber()));

        AccountType accountType = accountTypeRepository.findById(request.getAccountTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("AccountType", "id", request.getAccountTypeId()));

        Account account = new Account();
        account.setAccountNumber(request.getAccountNumber());

        // Sử dụng tên mặc định nếu không có accountName hoặc accountName trống
        String accountName = request.getAccountName();
        if (accountName == null || accountName.trim().isEmpty()) {
            accountName = AccountNameGenerator.generateDefaultAccountName(customer.getFullName());
        }
        account.setAccountName(accountName);

        account.setCustomer(customer);
        account.setAccountType(accountType);
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatus.ACTIVE);
        account = accountRepository.save(account);

        AccountResponse response = new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountName(),
                customer.getFullName(),
                accountType.getName(),
                account.getBalance(),
                account.getStatus(),
                account.getOpenedDate(),
                account.getClosedDate(),
                account.getCreateAt(),
                account.getUpdateAt());
        return ApiResponse.success("Account opened", response);
    }

    @Override
    public ApiResponse<AccountResponse> getByAccountNumber(String accountNumber, Authentication authentication) {
        // Use AccountResolver to find account by either accountId or accountNumber
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // Assuming userDetails is available
        Long customcustomerId = customerService.getCustomerByUserId(userDetails.getId()).getData().getId();
        Account account = accountResolver.validateAccountNumberAndCustomerId(accountNumber, customcustomerId);

        AccountResponse response = new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountName(),
                account.getCustomer().getFullName(),
                account.getAccountType().getName(),
                account.getBalance(),
                account.getStatus(),
                account.getOpenedDate(),
                account.getClosedDate(),
                account.getCreateAt(),
                account.getUpdateAt());
        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse<PageResponse<AccountResponse>> getAll(AccountFilterRequest filterRequest) {
        // Tạo Specification cho filtering
        Specification<Account> spec = createAccountSpecification(filterRequest);

        // Tạo Pageable cho pagination và sorting
        Sort sort = Sort.by(
                filterRequest.getSortDirection().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                filterRequest.getSortBy());
        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize(), sort);

        // Thực hiện query với pagination
        Page<Account> accountPage = accountRepository.findAll(spec, pageable);

        // Convert to DTO
        List<AccountResponse> accountResponses = accountPage.getContent().stream()
                .map(this::convertToAccountResponse)
                .toList();

        // Tạo PageResponse
        PageResponse<AccountResponse> pageResponse = new PageResponse<>(
                accountResponses,
                accountPage.getNumber(),
                accountPage.getSize(),
                accountPage.getTotalElements(),
                accountPage.getTotalPages(),
                accountPage.isFirst(),
                accountPage.isLast());

        return ApiResponse.success(pageResponse);
    }

    private Specification<Account> createAccountSpecification(AccountFilterRequest filterRequest) {
        return (root, query, criteriaBuilder) -> {
            Specification<Account> combinedSpec = Specification.anyOf();

            if (filterRequest.getAccountNumber() != null && !filterRequest.getAccountNumber().isEmpty()) {
                Specification<Account> currentSpecs = AccountSpecs.hasAccountNumber(filterRequest.getAccountNumber());
                combinedSpec.and(currentSpecs);
            }

            if (filterRequest.getAccountName() != null && !filterRequest.getAccountName().isEmpty()) {
                Specification<Account> currentSpecs = AccountSpecs.hasAccountName(filterRequest.getAccountName());
                combinedSpec.and(currentSpecs);
            }

            if (filterRequest.getCustomerId() != null) {
                Specification<Account> currentSpecs = AccountSpecs.hasCustomerId(filterRequest.getCustomerId());
                combinedSpec.and(currentSpecs);
            }

            if (filterRequest.getAccountTypeId() != null) {
                Specification<Account> currentSpecs = AccountSpecs.hasAccountTypeId(filterRequest.getAccountTypeId());
                combinedSpec.and(currentSpecs);
            }

            if (filterRequest.getStatus() != null) {
                Specification<Account> currentSpecs = AccountSpecs.hasStatus(filterRequest.getStatus());
                combinedSpec.and(currentSpecs);
            }

            if (filterRequest.getMinBalance() != null) {
                Specification<Account> currentSpecs = AccountSpecs.hasMinBalance(filterRequest.getMinBalance());
                combinedSpec.and(currentSpecs);
            }

            if (filterRequest.getMaxBalance() != null) {
                Specification<Account> currentSpecs = AccountSpecs.hasMaxBalance(filterRequest.getMaxBalance());
                combinedSpec.and(currentSpecs);
            }

            if (filterRequest.getOpenedFrom() != null) {
                Specification<Account> currentSpecs = AccountSpecs.openedFrom(filterRequest.getOpenedFrom());
                combinedSpec.and(currentSpecs);
            }

            if (filterRequest.getOpenedTo() != null) {
                Specification<Account> currentSpecs = AccountSpecs.openedTo(filterRequest.getOpenedTo());
                combinedSpec.and(currentSpecs);
            }

            return combinedSpec.toPredicate(root, query, criteriaBuilder);
        };

    }

    private AccountResponse convertToAccountResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountName(),
                account.getCustomer().getFullName(),
                account.getAccountType().getName(),
                account.getBalance(),
                account.getStatus(),
                account.getOpenedDate(),
                account.getClosedDate(),
                account.getCreateAt(),
                account.getUpdateAt());
    }

    @Override
    public ApiResponse<BigDecimal> getBalanceByAccountNumber(String accountNumber) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if (account.isPresent()) {
            return ApiResponse.success("Balance retrieved successfully", account.get().getBalance());
        } else {
            throw new ResourceNotFoundException("Account", "accountNumber", accountNumber);
        }
    }

    @Override
    @Transactional
    public ApiResponse<AccountResponse> updateAccount(Long id, UpdateAccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));

        account.setStatus(request.getStatus());
        if (request.getAccountName() != null && !request.getAccountName().trim().isEmpty()) {
            account.setAccountName(request.getAccountName());
        }
        if (request.getClosedDate() != null) {
            account.setClosedDate(request.getClosedDate());
        }
        account = accountRepository.save(account);

        AccountResponse response = convertToAccountResponse(account);
        return ApiResponse.success("Account updated successfully", response);
    }

    @Override
    @Transactional
    public ApiResponse<Void> closeAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));

        if (account.getStatus() == AccountStatus.CLOSED) {
            return new ApiResponse<>(true, "Account is already closed", null, LocalDateTime.now());
        }

        account.setStatus(AccountStatus.CLOSED);
        account.setClosedDate(LocalDateTime.now());
        accountRepository.save(account);

        return new ApiResponse<>(true, "Account closed successfully", null, LocalDateTime.now());
    }

    @Override
    public ApiResponse<List<AccountResponse>> getByCustomerId(Long customerId) {
        List<Account> accounts = accountRepository.findByCustomer_Id(customerId);
        List<AccountResponse> accountResponses = accounts.stream()
                .map(this::convertToAccountResponse)
                .toList();
        return ApiResponse.success(accountResponses);
    }

    @Override
    public ApiResponse<BigDecimal> getBalanceById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
        return ApiResponse.success(account.getBalance());
    }
}
