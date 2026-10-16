package web.lab.backend.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<DateValidation, LocalDate> {
    @Override
    public boolean isValid(
            LocalDate value,
            ConstraintValidatorContext context
    ) {
        if (value == null) {
            return true;
        }

        return value.isAfter(LocalDate.of(2000, 1, 1));
    }
}
