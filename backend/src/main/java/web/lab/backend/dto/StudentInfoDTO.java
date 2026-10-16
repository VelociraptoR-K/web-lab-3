package web.lab.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import web.lab.backend.dto.validation.DateValidation;
import web.lab.backend.dto.validation.NameValidation;

public record StudentInfoDTO(

    @NotBlank
    @NameValidation
    @Size(min = 2, max = 50)
    String name,

    @NotBlank
    @NameValidation
    @Size(min = 2, max = 50)
    String surname,

    @NameValidation
    @Size(max = 50)
    String patronymic,

    @NotBlank
    @Pattern(regexp = "^[A-Z]\\d{4}$")
    String group,

    @NotBlank
    @Pattern(regexp = "^[1-9]\\d{5}$")
    String isuId,

    @Min(1)
    @Max(100)
    Integer dormitoryNumber,

    @Min(1)
    @Max(1000)
    Integer room,

    @DateValidation
    @PastOrPresent
    LocalDate moveInDate,

    @NotNull
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
