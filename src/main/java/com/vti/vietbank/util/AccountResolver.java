package com.vti.vietbank.util;

import com.vti.vietbank.entity.Account;
import com.vti.vietbank.exception.ResourceNotFoundException;
import com.vti.vietbank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountResolver {
    
    private final AccountRepository accountRepository;
    
    public Account resolveAccount(Long accountId, String accountNumber) {
        if (accountId != null) {
            return accountRepository.findById(accountId)
                    .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));
        } else if (accountNumber != null && !accountNumber.trim().isEmpty()) {
            return accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber", accountNumber));
        } else {
            throw new IllegalArgumentException("Either accountId or accountNumber must be provided");
        }
    }
    
    public void validateAccountIdentifier(Long accountId, String accountNumber) {
        if (accountId == null && (accountNumber == null || accountNumber.trim().isEmpty())) {
            throw new IllegalArgumentException("Either accountId or accountNumber must be provided");
        }
        // Cho phép cung cấp cả hai, sẽ ưu tiên accountId
    }
    
    /**
     * Validates that exactly one account identifier is provided
     * @param accountId Account ID
     * @param accountNumber Account Number
     * @throws IllegalArgumentException if both or neither are provided
     */
    public void validateSingleAccountIdentifier(Long accountId, String accountNumber) {
        boolean hasAccountId = accountId != null;
        boolean hasAccountNumber = accountNumber != null && !accountNumber.trim().isEmpty();

        if (!hasAccountId && !hasAccountNumber) {
            throw new IllegalArgumentException("Either accountId or accountNumber must be provided");
        }

        if (hasAccountId && hasAccountNumber) {
            throw new IllegalArgumentException("Provide either accountId or accountNumber, not both");
        }
    }
    public void validateAccountNumberAndCustomerId(String accountNumber, Long customerId) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number must be provided");
        }
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID must be provided");
        }
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber", accountNumber));
        if (account.getCustomer().getId() != customerId) {
            throw new ResourceNotFoundException("Account", "accountNumber", accountNumber);
        }
    }
}
