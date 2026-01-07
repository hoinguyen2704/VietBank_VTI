package com.vti.vietbank.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class TransferAccountIdentifiersValidator implements ConstraintValidator<ValidTransferAccountIdentifiers, Object> {

    @Override
    public void initialize(ValidTransferAccountIdentifiers constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            // Get from account fields
            Field fromAccountIdField = obj.getClass().getDeclaredField("fromAccountId");
            fromAccountIdField.setAccessible(true);
            Integer fromAccountId = (Integer) fromAccountIdField.get(obj);

            Field fromAccountNumberField = obj.getClass().getDeclaredField("fromAccountNumber");
            fromAccountNumberField.setAccessible(true);
            String fromAccountNumber = (String) fromAccountNumberField.get(obj);

            // Get to account fields
            Field toAccountIdField = obj.getClass().getDeclaredField("toAccountId");
            toAccountIdField.setAccessible(true);
            Integer toAccountId = (Integer) toAccountIdField.get(obj);

            Field toAccountNumberField = obj.getClass().getDeclaredField("toAccountNumber");
            toAccountNumberField.setAccessible(true);
            String toAccountNumber = (String) toAccountNumberField.get(obj);

            // Validate from account
            boolean hasFromAccountId = fromAccountId != null;
            boolean hasFromAccountNumber = fromAccountNumber != null && !fromAccountNumber.trim().isEmpty();

            if (!hasFromAccountId && !hasFromAccountNumber) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Either fromAccountId or fromAccountNumber must be provided")
                        .addPropertyNode("fromAccountId")
                        .addConstraintViolation();
                return false;
            }

            if (hasFromAccountId && hasFromAccountNumber) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Provide either fromAccountId or fromAccountNumber, not both")
                        .addPropertyNode("fromAccountId")
                        .addConstraintViolation();
                return false;
            }

            // Validate to account
            boolean hasToAccountId = toAccountId != null;
            boolean hasToAccountNumber = toAccountNumber != null && !toAccountNumber.trim().isEmpty();

            if (!hasToAccountId && !hasToAccountNumber) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Either toAccountId or toAccountNumber must be provided")
                        .addPropertyNode("toAccountId")
                        .addConstraintViolation();
                return false;
            }

            if (hasToAccountId && hasToAccountNumber) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Provide either toAccountId or toAccountNumber, not both")
                        .addPropertyNode("toAccountId")
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
