package web.lab.backend.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator
        implements ConstraintValidator<NameValidation, String> {

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context
    ) {
        if (value == null || value.isBlank()) {
            return true;
        }

        return value.matches("\\p{L}[\\p{L} \\-`]*\\p{L}");
    }
}
