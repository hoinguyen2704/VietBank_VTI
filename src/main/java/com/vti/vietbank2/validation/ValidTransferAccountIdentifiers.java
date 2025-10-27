package com.vti.vietbank2.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TransferAccountIdentifiersValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTransferAccountIdentifiers {
    String message() default "Invalid account identifiers for transfer";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
