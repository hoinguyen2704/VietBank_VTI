package com.vti.vietbank2.validation;

import com.vti.vietbank2.dto.request.TransferRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransferAccountIdentifiersValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validateTransferRequest_WithValidAccountIds_ShouldPass() {
        // Given
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1);
        request.setToAccountId(2);
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1);

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void validateTransferRequest_WithValidAccountNumbers_ShouldPass() {
        // Given
        TransferRequest request = new TransferRequest();
        request.setFromAccountNumber("1000000000001");
        request.setToAccountNumber("1000000000002");
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1);

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void validateTransferRequest_WithMixedIdentifiers_ShouldPass() {
        // Given
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1);
        request.setToAccountNumber("1000000000002");
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1);

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void validateTransferRequest_WithBothFromAccountIdentifiers_ShouldFail() {
        // Given
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1);
        request.setFromAccountNumber("1000000000001");
        request.setToAccountId(2);
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1);

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> 
            v.getMessage().contains("Provide either fromAccountId or fromAccountNumber, not both")));
    }

    @Test
    void validateTransferRequest_WithBothToAccountIdentifiers_ShouldFail() {
        // Given
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1);
        request.setToAccountId(2);
        request.setToAccountNumber("1000000000002");
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1);

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> 
            v.getMessage().contains("Provide either toAccountId or toAccountNumber, not both")));
    }

    @Test
    void validateTransferRequest_WithNoFromAccountIdentifier_ShouldFail() {
        // Given
        TransferRequest request = new TransferRequest();
        request.setToAccountId(2);
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1);

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> 
            v.getMessage().contains("Either fromAccountId or fromAccountNumber must be provided")));
    }

    @Test
    void validateTransferRequest_WithNoToAccountIdentifier_ShouldFail() {
        // Given
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1);
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1);

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> 
            v.getMessage().contains("Either toAccountId or toAccountNumber must be provided")));
    }

    @Test
    void validateTransferRequest_WithEmptyFromAccountNumber_ShouldFail() {
        // Given
        TransferRequest request = new TransferRequest();
        request.setFromAccountNumber("");
        request.setToAccountId(2);
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1);

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> 
            v.getMessage().contains("Either fromAccountId or fromAccountNumber must be provided")));
    }

    @Test
    void validateTransferRequest_WithBlankToAccountNumber_ShouldFail() {
        // Given
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1);
        request.setToAccountNumber("   ");
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1);

        // When
        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> 
            v.getMessage().contains("Either toAccountId or toAccountNumber must be provided")));
    }
}
