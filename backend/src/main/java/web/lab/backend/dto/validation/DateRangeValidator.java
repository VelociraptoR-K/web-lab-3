package web.lab.backend.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import web.lab.backend.dto.students.DateRangeValidatable;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<DateRangeValidation, DateRangeValidatable> {

    @Override
    public boolean isValid(DateRangeValidatable value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDate from = value.getMoveInDateFrom();
        LocalDate to = value.getMoveInDateTo();

        if (from == null ^ to == null) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                            "moveInDateFrom and moveInDateTo must be specified together"
                    )
                    .addPropertyNode(from == null
                            ? "moveInDateFrom"
                            : "moveInDateTo")
                    .addConstraintViolation();

            return false;
        }

        if (from != null && from.isAfter(to)) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                            "moveInDateFrom must not be later than moveInDateTo"
                    )
                    .addPropertyNode("moveInDateFrom")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
