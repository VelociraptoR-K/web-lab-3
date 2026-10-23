package web.lab.backend.dto.students;

import java.time.LocalDate;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.*;
import web.lab.backend.dto.validation.DateValidation;
import web.lab.backend.dto.validation.NameValidation;

@JsonbNillable
public record StudentInfoDTO(

    @NotNull(message = "Parameter must be not blank")
    Long id,

    @NotBlank(message = "Parameter must be not blank")
    @NameValidation
    @Size(min = 2, max = 50, message = "Length must be from 2 to 50 symbols")
    String name,

    @NotBlank(message = "Parameter must be not blank")
    @NameValidation
    @Size(min = 2, max = 50, message = "Length must be from 2 to 50 symbols")
    String surname,

    @NameValidation
    @Size(max = 50, message = "Length must be up to 50 symbols")
    String patronymic,

    @NotBlank(message = "Parameter must be not blank")
    @Pattern(regexp = "^[A-Z]\\d{4}$", message = "Parameter must match the pattern")
    String group,

    @NotBlank(message = "Parameter must be not blank")
    @Pattern(regexp = "^[1-9]\\d{5}$", message = "Parameter must match the pattern")
    String isuId,

    @Min(value = 1, message = "Value must be greater than 1")
    @Max(value = 100, message = "Value must be less than 100")
    Integer dormitoryNumber,

    @Min(value = 1, message = "Value must be greater than 1")
    @Max(value = 1000, message = "Value must be less than 1000")
    Integer room,

    @DateValidation
    @PastOrPresent(message = "Date must be from 2000-01-01 to current date")
    LocalDate moveInDate,

    @NotNull(message = "Parameter must be not blank")
    Boolean foreigner,

    String notes
) {
    private static String trim(String value) {
        return value == null ? null : value.trim();
    }

    public StudentInfoDTO {
        name = trim(name);
        surname = trim(surname);
        patronymic = trim(patronymic);
        group = trim(group);
        isuId = trim(isuId);
        notes = trim(notes);
    }
}
