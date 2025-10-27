package com.vti.vietbank2.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class AccountIdentifierValidator implements ConstraintValidator<ValidAccountIdentifier, Object> {

    @Override
    public void initialize(ValidAccountIdentifier constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            // Get accountId field
            Field accountIdField = obj.getClass().getDeclaredField("accountId");
            accountIdField.setAccessible(true);
            Integer accountId = (Integer) accountIdField.get(obj);

            // Get accountNumber field
            Field accountNumberField = obj.getClass().getDeclaredField("accountNumber");
            accountNumberField.setAccessible(true);
            String accountNumber = (String) accountNumberField.get(obj);

            // Validate that exactly one is provided
            boolean hasAccountId = accountId != null;
            boolean hasAccountNumber = accountNumber != null && !accountNumber.trim().isEmpty();

            if (!hasAccountId && !hasAccountNumber) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Either accountId or accountNumber must be provided")
                        .addConstraintViolation();
                return false;
            }

            if (hasAccountId && hasAccountNumber) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Provide either accountId or accountNumber, not both")
                        .addConstraintViolation();
                return false;
            }

            return true;
        } catch (Exception e) {
            // If reflection fails, fall back to basic validation
            return true;
        }
    }
}
