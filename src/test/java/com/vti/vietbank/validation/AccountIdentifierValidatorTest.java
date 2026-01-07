package com.vti.vietbank.validation;

import com.vti.vietbank.dto.request.DepositRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AccountIdentifierValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validateDepositRequest_WithValidAccountId_ShouldPass() {
        // Given
        DepositRequest request = new DepositRequest();
        request.setAccountId(1L);
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1L);

        // When
        Set<ConstraintViolation<DepositRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void validateDepositRequest_WithValidAccountNumber_ShouldPass() {
        // Given
        DepositRequest request = new DepositRequest();
        request.setAccountNumber("1000000000001");
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1L);

        // When
        Set<ConstraintViolation<DepositRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void validateDepositRequest_WithBothAccountIdAndNumber_ShouldFail() {
        // Given
        DepositRequest request = new DepositRequest();
        request.setAccountId(1L);
        request.setAccountNumber("1000000000001");
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1L);

        // When
        Set<ConstraintViolation<DepositRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Provide either accountId or accountNumber, not both"));
    }

    @Test
    void validateDepositRequest_WithNeitherAccountIdNorNumber_ShouldFail() {
        // Given
        DepositRequest request = new DepositRequest();
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1L);

        // When
        Set<ConstraintViolation<DepositRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Either accountId or accountNumber must be provided"));
    }

    @Test
    void validateDepositRequest_WithEmptyAccountNumber_ShouldFail() {
        // Given
        DepositRequest request = new DepositRequest();
        request.setAccountNumber("");
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1L);

        // When
        Set<ConstraintViolation<DepositRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Either accountId or accountNumber must be provided"));
    }

    @Test
    void validateDepositRequest_WithBlankAccountNumber_ShouldFail() {
        // Given
        DepositRequest request = new DepositRequest();
        request.setAccountNumber("   ");
        request.setAmount(new BigDecimal("1000000"));
        request.setCreatedBy(1L);

        // When
        Set<ConstraintViolation<DepositRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Either accountId or accountNumber must be provided"));
    }
}
