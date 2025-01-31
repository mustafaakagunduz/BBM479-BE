package com.sms.hrsam.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        if (password.length() < 8) {
            context.buildConstraintViolationWithTemplate("Password must be at least 8 characters long")
                    .addConstraintViolation();
            isValid = false;
        }
        if (!password.matches(".*[A-Z].*")) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one uppercase letter")
                    .addConstraintViolation();
            isValid = false;
        }
        if (!password.matches(".*[a-z].*")) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one lowercase letter")
                    .addConstraintViolation();
            isValid = false;
        }
        if (!password.matches(".*\\d.*")) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one number")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}