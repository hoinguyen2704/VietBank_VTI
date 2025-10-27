package com.vti.vietbank2.util;

import com.vti.vietbank2.entity.Account;
import com.vti.vietbank2.exception.ResourceNotFoundException;
import com.vti.vietbank2.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountResolverTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountResolver accountResolver;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId(1);
        testAccount.setAccountNumber("1000000000001");
        testAccount.setAccountName("Test Account");
    }

    @Test
    void resolveAccount_WithValidAccountId_ShouldReturnAccount() {
        // Given
        when(accountRepository.findById(1)).thenReturn(Optional.of(testAccount));

        // When
        Account result = accountResolver.resolveAccount(1, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("1000000000001", result.getAccountNumber());
    }

    @Test
    void resolveAccount_WithValidAccountNumber_ShouldReturnAccount() {
        // Given
        when(accountRepository.findByAccountNumber("1000000000001")).thenReturn(Optional.of(testAccount));

        // When
        Account result = accountResolver.resolveAccount(null, "1000000000001");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("1000000000001", result.getAccountNumber());
    }

    @Test
    void resolveAccount_WithAccountId_ShouldPrioritizeAccountId() {
        // Given
        when(accountRepository.findById(1)).thenReturn(Optional.of(testAccount));

        // When
        Account result = accountResolver.resolveAccount(1, "1000000000001");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        // Verify that findByAccountNumber was not called
    }

    @Test
    void resolveAccount_WithInvalidAccountId_ShouldThrowException() {
        // Given
        when(accountRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> accountResolver.resolveAccount(999, null));
        
        assertEquals("Account not found with id: 999", exception.getMessage());
    }

    @Test
    void resolveAccount_WithInvalidAccountNumber_ShouldThrowException() {
        // Given
        when(accountRepository.findByAccountNumber("9999999999999")).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> accountResolver.resolveAccount(null, "9999999999999"));
        
        assertEquals("Account not found with accountNumber: 9999999999999", exception.getMessage());
    }

    @Test
    void resolveAccount_WithBothNull_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> accountResolver.resolveAccount(null, null));
        
        assertEquals("Either accountId or accountNumber must be provided", exception.getMessage());
    }

    @Test
    void resolveAccount_WithEmptyAccountNumber_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> accountResolver.resolveAccount(null, ""));
        
        assertEquals("Either accountId or accountNumber must be provided", exception.getMessage());
    }

    @Test
    void resolveAccount_WithBlankAccountNumber_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> accountResolver.resolveAccount(null, "   "));
        
        assertEquals("Either accountId or accountNumber must be provided", exception.getMessage());
    }

    @Test
    void validateAccountIdentifier_WithValidInput_ShouldNotThrowException() {
        // When & Then - should not throw exception
        assertDoesNotThrow(() -> accountResolver.validateAccountIdentifier(1, null));
        assertDoesNotThrow(() -> accountResolver.validateAccountIdentifier(null, "1000000000001"));
        assertDoesNotThrow(() -> accountResolver.validateAccountIdentifier(1, "1000000000001"));
    }

    @Test
    void validateAccountIdentifier_WithBothNull_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> accountResolver.validateAccountIdentifier(null, null));
        
        assertEquals("Either accountId or accountNumber must be provided", exception.getMessage());
    }

    @Test
    void validateSingleAccountIdentifier_WithValidInput_ShouldNotThrowException() {
        // When & Then - should not throw exception
        assertDoesNotThrow(() -> accountResolver.validateSingleAccountIdentifier(1, null));
        assertDoesNotThrow(() -> accountResolver.validateSingleAccountIdentifier(null, "1000000000001"));
    }

    @Test
    void validateSingleAccountIdentifier_WithBothProvided_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> accountResolver.validateSingleAccountIdentifier(1, "1000000000001"));
        
        assertEquals("Provide either accountId or accountNumber, not both", exception.getMessage());
    }

    @Test
    void validateSingleAccountIdentifier_WithBothNull_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> accountResolver.validateSingleAccountIdentifier(null, null));
        
        assertEquals("Either accountId or accountNumber must be provided", exception.getMessage());
    }
}
